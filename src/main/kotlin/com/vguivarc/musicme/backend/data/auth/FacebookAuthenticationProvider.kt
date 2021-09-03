package com.vguivarc.musicme.backend.data.auth

import com.vguivarc.musicme.backend.domain.models.account.AccountAuthentication
import com.vguivarc.musicme.backend.domain.models.auth.FacebookAuthenticationRequest
import com.vguivarc.musicme.backend.domain.models.social.FacebookUserModel
import com.vguivarc.musicme.backend.domain.providers.account.IAccountProvider
import com.vguivarc.musicme.backend.domain.providers.profile.IProfileProvider
import com.vguivarc.musicme.backend.libraries.errors.BaseExceptions
import com.vguivarc.musicme.backend.libraries.errors.DomainException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.server.ResponseStatusException

@Service
class FacebookAuthenticationProvider : AuthenticationProvider {

    @Autowired
    lateinit var accountProvider: IAccountProvider

    @Autowired
    lateinit var profileProvider: IProfileProvider

    override fun authenticate(authentication: Authentication?): Authentication {
        if (authentication !is FacebookAuthenticationRequest) {
            throw DomainException(BaseExceptions.ACCESS_DENIED)
        }
        val templateUrl: String =
            java.lang.String.format("/me?fields=email,first_name,last_name&access_token=%s", authentication.principal as String)
        val facebookUserModel: FacebookUserModel = WebClient
            .create("https://graph.facebook.com")
            .get().uri(templateUrl).retrieve()
            .onStatus(
                HttpStatus::isError
            ) { clientResponse ->
                throw ResponseStatusException(
                    clientResponse.statusCode(),
                    "facebook login error"
                )
            }
            .bodyToMono(FacebookUserModel::class.java)
            .block() ?: throw DomainException(BaseExceptions.ACCESS_DENIED)
        val account = accountProvider.findByFacebookId(facebookId = facebookUserModel.id?:"error")//.toAccount()
        return if(account==null){
            val newAccount = accountProvider.create(facebookUserModel.toAccount()).toAccount()
            profileProvider.create(facebookUserModel.toProfile(newAccount.id)).toProfile()
            return AccountAuthentication(newAccount)
        } else {
            AccountAuthentication(account.toAccount())
        }
    }

    override fun supports(authentication: Class<*>): Boolean {
        return authentication == FacebookAuthenticationRequest::class.java
    }
}
