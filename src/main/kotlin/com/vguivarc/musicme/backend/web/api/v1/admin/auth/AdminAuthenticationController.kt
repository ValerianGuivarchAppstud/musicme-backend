package com.vguivarc.musicme.backend.web.api.v1.admin.auth

import com.vguivarc.musicme.backend.domain.models.nested.Authority
import com.vguivarc.musicme.backend.domain.services.AuthenticationService
import com.vguivarc.musicme.backend.libraries.errors.BaseExceptions
import com.vguivarc.musicme.backend.libraries.errors.DomainException
import com.vguivarc.musicme.backend.web.api.v1.admin.auth.entities.JwtAuthAdminResponseVM
import com.vguivarc.musicme.backend.web.api.v1.admin.auth.requests.BoLoginWithPasswordRequest
import com.vguivarc.musicme.backend.web.api.v1.client.auth.requests.JwtRefreshAuthRequest
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@CrossOrigin
@RequestMapping("/api/v1/admin/auth")
@RestController
@Api(value = "admin-auth")
class AdminAuthenticationController {

    @Autowired
    lateinit var authenticationService: AuthenticationService

    @PostMapping("/login")
    @ApiOperation(value = "admin > auth > login")
    fun login(
        @Valid
        @RequestBody
        @ApiParam(value = "the request to login", required = true)
        authRequest: BoLoginWithPasswordRequest
    ): JwtAuthAdminResponseVM {
        val auth = authenticationService.authenticateByPassword(authRequest.email, authRequest.password)
        if (auth.authority != Authority.ADMIN) throw DomainException(BaseExceptions.UNAUTHORIZED)

        return JwtAuthAdminResponseVM.fromJwtAuthResponse(auth)
    }

    @PostMapping("/refresh")
    @ApiOperation(value = "admin > auth > refresh access token")
    fun refreshAuth(
        @Valid
        @RequestBody
        @ApiParam(value = "the request to refresh the access token", required = true)
        authRefreshRequest: JwtRefreshAuthRequest
    ): JwtAuthAdminResponseVM {
        return JwtAuthAdminResponseVM.fromJwtAuthResponse(
            authenticationService.refreshToken(authRefreshRequest.accessToken, authRefreshRequest.refreshToken)
        )
    }
}
