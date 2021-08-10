package com.vguivarc.musicme.backend.libraries.jwt

import com.vguivarc.musicme.backend.domain.providers.account.IAccountProvider
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import java.nio.charset.StandardCharsets
import java.security.Key

class SigningKeyResolver(
    private val accountProvider: IAccountProvider,
    private val jwtProperties: JwtProperties
) : SigningKeyResolverAdapter() {

    override fun resolveSigningKey(header: JwsHeader<out JwsHeader<*>>?, claims: Claims?): Key {
        if (claims == null) throw UnsupportedJwtException("Empty claims")
        val id = claims["sub"] ?: throw UnsupportedJwtException("Empty claims subject")
        val account = accountProvider.findById(id as String)?.toAccount() ?: throw JwtException("Account not found")
        return Keys.hmacShaKeyFor(
            "${jwtProperties.secretKey}${account.secret}".toByteArray(StandardCharsets.UTF_8)
        )
    }
}
