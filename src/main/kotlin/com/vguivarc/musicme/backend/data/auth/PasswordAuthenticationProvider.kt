package com.vguivarc.musicme.backend.data.auth

import com.vguivarc.musicme.backend.domain.models.account.AccountAuthentication
import com.vguivarc.musicme.backend.domain.models.auth.PasswordAuthenticationRequest
import com.vguivarc.musicme.backend.domain.providers.account.IAccountProvider
import com.vguivarc.musicme.backend.libraries.errors.BaseExceptions
import com.vguivarc.musicme.backend.libraries.errors.DomainException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class PasswordAuthenticationProvider : AuthenticationProvider {

    @Autowired
    lateinit var accountProvider: IAccountProvider

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    override fun authenticate(authentication: Authentication?): Authentication {
        if (authentication !is PasswordAuthenticationRequest) {
            throw DomainException(BaseExceptions.ACCESS_DENIED)
        }

        val account = accountProvider.findOneByEmail(
            email = authentication.principal as String
        ).toAccount()

        if (!passwordEncoder.matches(authentication.credentials as String, account.password)) {
            throw DomainException(BaseExceptions.ACCESS_DENIED)
        }

        return AccountAuthentication(account)
    }

    override fun supports(authentication: Class<*>): Boolean {
        return authentication == PasswordAuthenticationRequest::class.java
    }
}
