package com.vguivarc.musicme.backend.web.api.v1.user.auth

import com.vguivarc.musicme.backend.domain.models.auth.JwtAuthResponse
import com.vguivarc.musicme.backend.domain.models.profile.Profile
import com.vguivarc.musicme.backend.domain.services.AccountService
import com.vguivarc.musicme.backend.domain.services.AuthenticationService
import com.vguivarc.musicme.backend.domain.services.ProfileService
import com.vguivarc.musicme.backend.domain.services.SenderService
import com.vguivarc.musicme.backend.libraries.errors.DomainException
import com.vguivarc.musicme.backend.libraries.errors.DomainExceptions
import com.vguivarc.musicme.backend.web.api.v1.user.auth.entities.JwtAuthResponseVM
import com.vguivarc.musicme.backend.web.api.v1.user.auth.requests.*
import com.vguivarc.musicme.backend.web.api.v1.user.AccountVM
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.constraints.Email
import com.vguivarc.musicme.backend.web.api.v1.user.auth.entities.JwtAuthResponseVM.Companion as JwtAuthResponseVM1

@RequestMapping("/api/v1/user/auth")
@RestController
@Api(
    value = "user authentication",
    description = "handle the users authentication"
)
class UserAuthenticationController {

    @Autowired
    lateinit var authenticationService: AuthenticationService

    @Autowired
    lateinit var accountService: AccountService

    @Autowired
    lateinit var profileService: ProfileService

    @Autowired
    lateinit var senderService: SenderService

    /**
     * MAIL OTP
     */
    @PostMapping("/login/email/sendCode")
    fun sendEmailCode(@Valid @RequestParam @Email email: String) {
        val code = authenticationService.generateTokenByMail(email)
        return senderService.sendVerificationCodeByMail(email, code)
    }

    @PostMapping("/login/email")
    fun checkValidOneTimeCodeByMail(
        @Valid
        @RequestParam
        code: String,
        @Valid
        @RequestParam
        email: String
    ): JwtAuthResponse {
        val auth = authenticationService.authenticateByMail(email, code)
        authenticationService.findOrCreateAccountByMail(email)
        return auth
    }

    /**
     * MAIL - PASSWORD
     */
    @PostMapping("/register/mail")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "register a user")
    fun registerMail(
        @Valid
        @RequestBody
        @ApiParam(value = "the request to register a new user", required = true)
        registerWithPasswordRequest: RegisterWithPasswordRequest
    ): AccountVM {
        val accountReq = registerWithPasswordRequest.toAccount()
        val email = accountReq.email ?: throw DomainException(DomainExceptions.ACCOUNT_NOT_FOUND)

        if (accountService.existByEmail(email)) {
            throw DomainException(DomainExceptions.ACCOUNT_ALREADY_EXISTS)
        }

        val account = accountService.createAccount(accountReq)

        val profile = profileService.createProfile(
            Profile(
                idAccount = account.idAccount,
                username = registerWithPasswordRequest.username
            )
        )

        return AccountVM.fromAccountAndProfile(account, profile)




    }

    @PostMapping("/login/mail")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "login with a password")
    fun loginMail(
        @Valid
        @RequestBody
        @ApiParam(value = "the request to login with a password", required = true)
        loginWithPasswordRequest: LoginWithPasswordRequest
    ): JwtAuthResponseVM {
        val auth = authenticationService.authenticateByPassword(
            loginWithPasswordRequest.email,
            loginWithPasswordRequest.password
        )
        return JwtAuthResponseVM1.fromJwtAuthResponse(auth)
    }


    /**
     * Facebook
     */

    @PostMapping("/login/facebook")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "login with a password")
    fun loginFacebook(
        @Valid
        @RequestBody
        @ApiParam(value = "the request to login with a password", required = true)
        loginWithFacebookRequest: LoginWithFacebookRequest
    ): JwtAuthResponseVM {
        val auth = authenticationService.authenticateByFacebookToken(
            loginWithFacebookRequest.facebookToken
        )
        return JwtAuthResponseVM1.fromJwtAuthResponse(auth)
    }

    /**
     * REFRESH TOKEN
     */
    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "refresh the authentication token")
    fun refreshAuth(
        @Valid
        @RequestBody
        @ApiParam(
            value = "the request to refresh the token",
            required = true
        )
        authRefreshRequest: JwtRefreshAuthRequest
    ): JwtAuthResponseVM {
        return JwtAuthResponseVM1.fromJwtAuthResponse(
            authenticationService.refreshToken(
                authRefreshRequest.accessToken,
                authRefreshRequest.refreshToken
            )
        )
    }


    @PutMapping("/firebaseToken")
    fun addFirebaseToken(
        @Valid
        @RequestBody
        @ApiParam(value = "the request to register a new user", required = true)
        firebaseTokenRequest: FirebaseTokenRequest
    ) {
        val auth = authenticationService.addFirebaseToken(firebaseTokenRequest.token)
    }
}
