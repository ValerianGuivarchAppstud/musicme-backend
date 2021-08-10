package com.vguivarc.musicme.backend.libraries.jwt

import com.vguivarc.musicme.backend.domain.providers.account.IAccountProvider
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.security.core.token.Token

class JwtToken(
    val accountProvider: IAccountProvider,
    private val jwtProperties: JwtProperties
) : Token {

    var claims: Claims = Jwts.claims()
        set(value) {
            field.putAll(value)
        }

    var token: String? = null
        set(value) {
            claims = Jwts.parserBuilder()
                .setSigningKeyResolver(SigningKeyResolver(accountProvider, jwtProperties))
                .build()
                .parseClaimsJws(value)?.body ?: return
            field = value
        }

    fun getSubject(): String {
        return claims.subject
    }

    fun isRefreshToken(): Boolean {
        return claims[JwtConstants.ACCESS_TOKEN_KEY] != null
    }

    fun getAccessToken(): String? {
        return claims[JwtConstants.ACCESS_TOKEN_KEY]?.toString()
    }

    override fun getKey(): String {
        return token ?: ""
    }

    override fun getExtendedInformation(): String {
        return "${claims.subject};${claims[JwtConstants.AUTHORITIES_KEY]}"
    }

    override fun getKeyCreationTime(): Long {
        return claims.expiration.time
    }
}
