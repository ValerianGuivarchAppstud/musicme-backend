package com.vguivarc.musicme.backend.web.api.v1.client.auth

import com.vguivarc.musicme.backend.domain.models.auth.JwtAuthResponse
import com.vguivarc.musicme.backend.domain.models.nested.AccountStatus
import com.vguivarc.musicme.backend.domain.models.profile.Profile
import com.vguivarc.musicme.backend.domain.services.AccountService
import com.vguivarc.musicme.backend.domain.services.AuthenticationService
import com.vguivarc.musicme.backend.domain.services.ProfileService
import com.vguivarc.musicme.backend.domain.services.SenderService
import com.vguivarc.musicme.backend.libraries.errors.DomainException
import com.vguivarc.musicme.backend.libraries.errors.DomainExceptions
import com.vguivarc.musicme.backend.web.api.v1.client.auth.entities.JwtAuthResponseVM
import com.vguivarc.musicme.backend.web.api.v1.client.auth.requests.*
import com.vguivarc.musicme.backend.web.api.v1.common.entities.AccountVM
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.constraints.Email
import com.vguivarc.musicme.backend.web.api.v1.client.auth.entities.JwtAuthResponseVM.Companion as JwtAuthResponseVM1

@RequestMapping("/api/v1/client/auth")
@RestController
@Api(
    value = "client authentication",
    description = "handle the clients authentication"
)
class ClientAuthenticationController {

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
     * DEVICE
     */
    @PostMapping("/register/device")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "register a device")
    fun registerWithDevice(
        @Valid
        @RequestBody
        @ApiParam(value = "the request to register a new device", required = true)
        registerWithDeviceRequest: RegisterWithDeviceRequest
    ): AccountVM {
        val accountReq = registerWithDeviceRequest.toAccount()
        val deviceId = accountReq.deviceId ?: throw DomainException(DomainExceptions.ACCOUNT_NOT_FOUND)

        if (accountService.existsByDeviceId(deviceId))
            throw DomainException(DomainExceptions.ACCOUNT_ALREADY_EXISTS)

        val profile = profileService.findProfileWithIdAccount(accountReq.id)

        return AccountVM.fromAccountAndProfile(
            accountService.createAccount(accountReq),
            profile
        )
    }

    @PostMapping("/login/device")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "login with a device id")
    fun loginWithDevice(
        @Valid
        @RequestBody
        @ApiParam(value = "the request to login with a device id", required = true)
        loginWithDeviceRequest: LoginWithDeviceRequest
    ): JwtAuthResponseVM {
        val auth = authenticationService.authenticateByDeviceId(loginWithDeviceRequest.deviceId)
        val authResponse = JwtAuthResponseVM1.fromJwtAuthResponse(auth)
        val account = accountService.findByDeviceId(loginWithDeviceRequest.deviceId)
        if (account.status == AccountStatus.NEW) {
            accountService.validateAccount(account)
        }

        return authResponse
    }

    /**
     * PASSWORD
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "register a client")
    fun register(
        @Valid
        @RequestBody
        @ApiParam(value = "the request to register a new client", required = true)
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
                idAccount = account.id
            )
        )

        return AccountVM.fromAccountAndProfile(account, profile)
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "login with a password")
    fun login(
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
}
