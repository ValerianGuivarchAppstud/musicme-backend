package com.vguivarc.musicme.backend.data.auth

import com.vguivarc.musicme.backend.domain.models.account.AccountAuthentication
import com.vguivarc.musicme.backend.domain.models.auth.DeviceAuthenticationRequest
import com.vguivarc.musicme.backend.domain.providers.account.IAccountProvider
import com.vguivarc.musicme.backend.libraries.errors.BaseExceptions
import com.vguivarc.musicme.backend.libraries.errors.DomainException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service

@Service
class DeviceAuthenticationProvider : AuthenticationProvider {

    @Autowired
    lateinit var accountProvider: IAccountProvider

    override fun authenticate(authentication: Authentication?): Authentication {
        if (authentication !is DeviceAuthenticationRequest) {
            throw DomainException(BaseExceptions.ACCESS_DENIED)
        }
        val account = accountProvider.findByDeviceId(deviceId = authentication.principal as String).toAccount()
        return AccountAuthentication(account)
    }

    override fun supports(authentication: Class<*>): Boolean {
        return authentication == DeviceAuthenticationRequest::class.java
    }
}
