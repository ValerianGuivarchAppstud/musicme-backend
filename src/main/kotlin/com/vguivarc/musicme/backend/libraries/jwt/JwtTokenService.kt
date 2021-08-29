package com.vguivarc.musicme.backend.libraries.jwt

import com.sun.security.auth.UserPrincipal
import com.vguivarc.musicme.backend.domain.models.auth.AccessToken
import com.vguivarc.musicme.backend.domain.providers.account.IAccountProvider
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.token.Token
import org.springframework.security.core.token.TokenService
import java.nio.charset.StandardCharsets
import java.security.Key
import java.util.*
import java.util.stream.Collectors
import javax.annotation.PostConstruct

// Abstract class complexity cannot really be reduced. It handles
@Configuration
@Suppress("TooManyFunctions")
class JwtTokenService(val jwtProperties: JwtProperties) : TokenService {
    companion object {
        const val AUTHORIZATION_HEADER = "Authorization"
    }

    @Autowired
    lateinit var accountProvider: IAccountProvider

    @Bean
    fun jwtToken(): JwtToken {
        return JwtToken(
            accountProvider = accountProvider,
            jwtProperties = jwtProperties
        )
    }

    protected lateinit var key: Key

    @PostConstruct
    fun init() {
        key = Keys.hmacShaKeyFor(jwtProperties.secretKey.toByteArray(StandardCharsets.UTF_8))
    }

    fun createKey(secret: String): Key {
        return Keys.hmacShaKeyFor("${jwtProperties.secretKey}$secret".toByteArray(StandardCharsets.UTF_8))
    }

    private fun unpackToken(token: String?): JwtToken {
        return jwtToken().apply { this.token = token }
    }

    override fun allocateToken(actualToken: String?): JwtToken {
        return unpackToken(actualToken)
    }

    fun allocateAccessToken(authentication: Authentication): Token {
        val authorities =
            authentication.authorities.stream()
                .map { it.authority }
                .collect(Collectors.joining(","))

        val validity = Date(accessExpirationDate())
        val token = jwtToken()
        val jwts = Jwts.builder()
            .setSubject(authentication.name)
            .claim(JwtConstants.AUTHORITIES_KEY, authorities)
            .signWith(createKey(authentication.credentials as String), SignatureAlgorithm.HS512)
            .setExpiration(validity)
            .compact()
        token.token = jwts
        return token
    }

    fun allocateRefreshToken(authentication: Authentication, accessToken: String?): Token {
        val validity = Date(refreshExpirationDate())
        val token = jwtToken()
        val key = Jwts.builder()
            .setSubject(authentication.name)
            .claim(JwtConstants.ACCESS_TOKEN_KEY, accessToken)
            .signWith(createKey(authentication.credentials as String), SignatureAlgorithm.HS512)
            .setExpiration(validity)
            .compact()
        token.token = key
        return token
    }

    fun canRefresh(accessToken: String, refreshToken: Token?): Boolean {
        if (refreshToken !is JwtToken) return false
        return refreshToken.key.isNotEmpty() &&
            refreshToken.isRefreshToken() &&
            refreshToken.getAccessToken() == accessToken
    }

    fun getAuthentication(token: Token): Authentication {
        if (token !is JwtToken || token.key.isEmpty()) throw UnsupportedJwtException(
            "Token is not compatible"
        )
        return if (token.isRefreshToken()) {
            createAuthenticationFromRefreshToken(token)
        } else {
            createAuthentication(token)
        }
    }

    private fun createAuthenticationFromRefreshToken(refreshToken: JwtToken): Authentication {
        if (!refreshToken.isRefreshToken()) throw UnsupportedJwtException(
            "Not a refresh token"
        )
        return try {
            // Creates an auth from not expired token
            createAuthentication(unpackToken(refreshToken.getAccessToken()))
        } catch (e: ExpiredJwtException) {
            // Creates auth from expired token
            createAuthentication(e.claims)
        }
    }

    fun createAuthentication(claims: Claims): Authentication {
        val login = claims.subject
        // add the authorities to the access token
        return AccessToken(UserPrincipal(login), "", listOf(SimpleGrantedAuthority("USER")))
    }

    fun createAuthentication(key: JwtToken): Authentication {
        return createAuthentication(key.claims)
    }

    override fun verifyToken(key: String?): JwtToken {
        val jwtToken: JwtToken?
        try {
            jwtToken = this.unpackToken(key)
            val id = jwtToken.getSubject()
            val account = accountProvider.findById(id)?.toAccount() ?: throw JwtException("Account not found")
            Jwts.parserBuilder()
                .setSigningKey(createKey(account.secret ?: ""))
                .build()
                .parse(jwtToken.token)
        } catch (e: JwtException) {
            throw handleJWTException(e)
        }
        return jwtToken
    }

    private fun handleJWTException(e: Exception): Throwable {
        return when (e) {
            is SignatureException -> InvalidTokenException(
                "Invalid JWT signature.",
                e
            )
            is MalformedJwtException -> InvalidTokenException(
                "Invalid JWT token.",
                e
            )
            is ExpiredJwtException -> InvalidTokenException(
                "Expired JWT token.",
                e
            )
            is UnsupportedJwtException -> InvalidTokenException(
                "Unsupported JWT token.",
                e
            )
            is IllegalArgumentException -> IllegalArgumentException(
                "JWT token compact of handler are invalid.",
                e
            )
            is JwtException -> InvalidTokenException(
                e.localizedMessage,
                e
            )
            else -> e
        }
    }

    fun refreshExpirationDate(): Long {
        return Date().time + jwtProperties.rememberTimeToLive
    }

    fun accessExpirationDate(): Long {
        return Date().time + jwtProperties.timeToLive
    }
}
