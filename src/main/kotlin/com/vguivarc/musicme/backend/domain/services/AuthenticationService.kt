package com.vguivarc.musicme.backend.domain.services

import com.vguivarc.musicme.backend.domain.models.VerificationCode
import com.vguivarc.musicme.backend.domain.models.account.Account
import com.vguivarc.musicme.backend.domain.models.account.AccountAuthentication
import com.vguivarc.musicme.backend.domain.models.auth.EmailAuthenticationRequest
import com.vguivarc.musicme.backend.domain.models.auth.FacebookAuthenticationRequest
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
        return JwtAuthResponse(
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

// FACEBOOK


    fun authenticateByFacebookToken(facebookToken: String): JwtAuthResponse {
        val request = FacebookAuthenticationRequest(facebookToken)
        val authentication = authenticationProvider.authenticate(request)
        return generateAuthResponse(authentication)
        /*
        val userOptional: Optional<User> = authenticationProvider.findByEmail(facebookUserModel.email)
        return if (userOptional.isEmpty()) {        //we have no user with given email so register them
            val user =
                User(facebookUserModel.email, RandomString(10).nextString(), LoginMethodEnum.FACEBOOK, "ROLE_USER")
            userRepository.save(user)
            val userPrincipal = UserPrincipal(user)
            val jwt: String = tokenProvider.generateToken(userPrincipal)
            val location: URI = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/v1/user/{username}")
                .buildAndExpand(facebookUserModel.getFirstName()).toUri()
            ResponseEntity.created(location).body<Any>(LoginResponse(Properties.TOKEN_PREFIX.toString() + jwt))
        } else { // user exists just login
            val user: User = userOptional.get()
            if (user.getLoginMethodEnum() !== LoginMethodEnum.FACEBOOK) { //check if logged in with different logged in method
                return ResponseEntity.badRequest().body("previously logged in with different login method")
            }
            val userPrincipal = UserPrincipal(user)
            val jwt: String = tokenProvider.generateTokenWithPrinciple(userPrincipal)
            ResponseEntity.ok<Any>(LoginResponse(Properties.TOKEN_PREFIX.toString() + jwt))
        }*/
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

    fun addFirebaseToken(token: String) {

    }

    fun generateTokenByMail(mail: String): VerificationCode {
        val email = mail.sanitizeEmail()
        val account = accountProvider.findOneByEmail(email).toAccount()
        return verificationCodeProvider.addVerificationCode(account.idAccount).toVerificationCode()
    }

// Authenticated
    fun findConnectedAccount(): Account {
        val accountId = getConnectedUserId()
        return accountProvider.findOneByIdAccount(accountId).toAccount()
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
        val account = accountProvider.findOneByIdAccount(accountId).toAccount()
        if (account.status != AccountStatus.NEW && account.status != AccountStatus.ACTIVE) {
            throw DomainException(BaseExceptions.UNAUTHORIZED)
        }
        return account
    }

    fun findListByFacebookId(facebookId: List<String>?) : List<Account>{
        facebookId?.let { facebookIdList ->
            val accountId = getConnectedUserId()
            if ("anonymousUser" == accountId) {
                throw DomainException(BaseExceptions.UNAUTHORIZED)
            }
            val res =  facebookIdList.mapNotNull { accountProvider.findOneByFacebookIdOrNull(it)?.toAccount() }
            return res
        } ?:let {
            throw DomainException(BaseExceptions.RESOURCE_NOT_FOUND)
        }

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
        val account = accountProvider.findOneByIdAccount(auth.principal.toString()).toAccount()
        return generateAuthResponse(AccountAuthentication(account))
    }
}
