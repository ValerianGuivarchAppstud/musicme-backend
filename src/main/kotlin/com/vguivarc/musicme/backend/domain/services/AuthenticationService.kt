package com.vguivarc.musicme.backend.domain.services

import com.vguivarc.musicme.backend.domain.models.VerificationCode
import com.vguivarc.musicme.backend.domain.models.account.Account
import com.vguivarc.musicme.backend.domain.models.account.AccountAuthentication
import com.vguivarc.musicme.backend.domain.models.auth.DeviceAuthenticationRequest
import com.vguivarc.musicme.backend.domain.models.auth.EmailAuthenticationRequest
import com.vguivarc.musicme.backend.domain.models.auth.JwtAuthResponse
import com.vguivarc.musicme.backend.domain.models.auth.PasswordAuthenticationRequest
import com.vguivarc.musicme.backend.domain.models.nested.AccountStatus
import com.vguivarc.musicme.backend.domain.providers.account.IAccountProvider
import com.vguivarc.musicme.backend.domain.providers.verificationcode.IVerificationCodeProvider
import com.vguivarc.musicme.backend.libraries.errors.BaseExceptions
import com.vguivarc.musicme.backend.libraries.errors.DomainException
import com.vguivarc.musicme.backend.libraries.jwt.InvalidTokenException
import com.vguivarc.musicme.backend.libraries.jwt.JwtTokenService
import com.vguivarc.musicme.backend.utils.sanitizeEmail
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class AuthenticationService {

    val logger: Logger = LoggerFactory.getLogger(AuthenticationService::class.java)

    @Autowired
    lateinit var authenticationProvider: AuthenticationManager

    @Autowired
    lateinit var verificationCodeProvider: IVerificationCodeProvider

    @Autowired
    lateinit var accountProvider: IAccountProvider

    @Autowired
    lateinit var jwtTokenService: JwtTokenService

    private fun generateAuthResponse(authentication: Authentication): JwtAuthResponse {
        val accessToken = jwtTokenService.allocateAccessToken(authentication).key
        val accessTokenExpiration = jwtTokenService.accessExpirationDate()
        val refreshToken = jwtTokenService.allocateRefreshToken(authentication, accessToken)
        val refreshTokenExpirationDate = jwtTokenService.refreshExpirationDate()
        val accountId = authentication.principal.toString()
        val authority = accountProvider.findOneById(accountId).toAccount().authority
        return JwtAuthResponse(
            authority,
            accountId,
            accessToken,
            accessTokenExpiration,
            refreshToken.key,
            refreshTokenExpirationDate
        )
    }

    /**
     * get the connected user principal, returns 'anonymousUser' if the token is expired/invalid
     * @return String
     */
    private fun getConnectedUserId(): String {
        val auth = SecurityContextHolder.getContext().authentication
        return auth.principal.toString()
    }

// DEVICE ID

    fun authenticateByDeviceId(deviceId: String): JwtAuthResponse {
        val request = DeviceAuthenticationRequest(deviceId)
        val authentication = authenticationProvider.authenticate(request)
        return generateAuthResponse(authentication)
    }

// PASSWORD

    fun authenticateByPassword(mail: String, password: String): JwtAuthResponse {
        val request = PasswordAuthenticationRequest(mail, password)
        val authentication = authenticationProvider.authenticate(request)
        return generateAuthResponse(authentication)
    }

// MAIL

    fun findOrCreateAccountByMail(mail: String): Account {
        return accountProvider.findOrCreateAccountByEmail(mail).toAccount()
    }

    fun authenticateByMail(mail: String, code: String): JwtAuthResponse {
        val email = mail.sanitizeEmail()
        val request = EmailAuthenticationRequest(email, code)
        val authentication = authenticationProvider.authenticate(request)
        return generateAuthResponse(authentication)
    }

    fun generateTokenByMail(mail: String): VerificationCode {
        val email = mail.sanitizeEmail()
        val account = accountProvider.findOneByEmail(email).toAccount()
        return verificationCodeProvider.addVerificationCode(account.id).toVerificationCode()
    }

// Authenticated
    fun findConnectedAccount(): Account {
        val accountId = getConnectedUserId()
        return accountProvider.findOneById(accountId).toAccount()
    }

    /**
     * get the connected user, or throw an 'unauthorized' exception
     * @return Account
     */
    fun findConnectedAccountOrThrowAccessDenied(): Account {
        val accountId = getConnectedUserId()
        if ("anonymousUser" == accountId) {
            throw DomainException(BaseExceptions.UNAUTHORIZED)
        }
        val account = accountProvider.findOneById(accountId).toAccount()
        if (account.status != AccountStatus.NEW && account.status != AccountStatus.ACTIVE) {
            throw DomainException(BaseExceptions.UNAUTHORIZED)
        }
        return account
    }

    fun refreshToken(accessTokenString: String, refreshTokenString: String): JwtAuthResponse {
        val refreshToken = try {
            jwtTokenService.verifyToken(refreshTokenString)
        } catch (invalidTokenException: InvalidTokenException) {
            this.logger.error(invalidTokenException.localizedMessage)
            throw DomainException(BaseExceptions.ACCESS_DENIED)
        }
        if (!jwtTokenService.canRefresh(accessTokenString, refreshToken)) {
            throw DomainException(BaseExceptions.ACCESS_DENIED)
        }
        val auth = jwtTokenService.getAuthentication(refreshToken)
        val account = accountProvider.findOneById(auth.principal.toString()).toAccount()
        return generateAuthResponse(AccountAuthentication(account))
    }
}
