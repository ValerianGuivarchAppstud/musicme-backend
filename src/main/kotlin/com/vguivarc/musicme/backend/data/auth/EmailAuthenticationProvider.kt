package com.vguivarc.musicme.backend.data.auth

import com.vguivarc.musicme.backend.domain.models.account.AccountAuthentication
import com.vguivarc.musicme.backend.domain.models.auth.EmailAuthenticationRequest
import com.vguivarc.musicme.backend.domain.providers.account.IAccountProvider
import com.vguivarc.musicme.backend.domain.providers.verificationcode.IVerificationCodeProvider
import com.vguivarc.musicme.backend.libraries.errors.BaseExceptions
import com.vguivarc.musicme.backend.libraries.errors.DomainException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.time.ZonedDateTime

@Service
class EmailAuthenticationProvider : AuthenticationProvider {

    @Autowired
    lateinit var accountProvider: IAccountProvider

    @Autowired
    lateinit var verificationProvider: IVerificationCodeProvider

    override fun authenticate(authentication: Authentication?): Authentication {
        if (authentication !is EmailAuthenticationRequest) {
            throw DomainException(BaseExceptions.ACCESS_DENIED)
        }
        val account = accountProvider.findOneByEmail(email = authentication.principal as String).toAccount()

        verificationProvider.findByAccount(account.idAccount)
            .asSequence()
            .map { it.toVerificationCode() }
            .filter { !it.isUsed && it.expirationDate.isAfter(ZonedDateTime.now()) }
            .sortedByDescending { it.expirationDate }
            .filter { it.code == authentication.credentials }
            .firstOrNull() ?: throw DomainException(BaseExceptions.ACCESS_DENIED)

        verificationProvider.findByAccount(account.idAccount)
            .map { it.toVerificationCode() }
            .forEach {
                verificationProvider.markAsUsed(it.id)
            }
        return AccountAuthentication(account)
    }

    override fun supports(authentication: Class<*>): Boolean {
        return authentication == EmailAuthenticationRequest::class.java
    }
}
