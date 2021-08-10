package com.vguivarc.musicme.backend.web.api.v1.client.profile

import com.vguivarc.musicme.backend.domain.models.nested.Authority
import com.vguivarc.musicme.backend.domain.services.AccountService
import com.vguivarc.musicme.backend.domain.services.AuthenticationService
import com.vguivarc.musicme.backend.domain.services.ProfileService
import com.vguivarc.musicme.backend.libraries.errors.BaseExceptions
import com.vguivarc.musicme.backend.libraries.errors.DomainException
import com.vguivarc.musicme.backend.web.api.v1.client.profile.requests.UpdateProfileRequest
import com.vguivarc.musicme.backend.web.api.v1.common.entities.AccountVM
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RequestMapping("/api/v1/client/profile")
@RestController
@Api(
    value = "client profile",
    description = "handle the clients profile requests"
)
class ClientProfileController {

    @Autowired
    lateinit var authenticationService: AuthenticationService

    @Autowired
    lateinit var accountService: AccountService

    @Autowired
    lateinit var profileService: ProfileService

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "get the client profile")
    fun getAccount(): AccountVM {
        val account = authenticationService.findConnectedAccountOrThrowAccessDenied()

        if (account.authority != Authority.USER) {
            throw DomainException(BaseExceptions.UNAUTHORIZED)
        }

        val profile = profileService.findProfileWithIdAccount(account.id)

        return AccountVM.fromAccountAndProfile(account, profile)
    }

    @PutMapping("")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "updates the client profile")
    fun updateProfile(
        @Valid
        @RequestBody
        @ApiParam(value = "the request to update the profile", required = true)
        updateProfileRequest: UpdateProfileRequest
    ): AccountVM {
        val account = authenticationService.findConnectedAccountOrThrowAccessDenied()

        if (account.authority != Authority.USER) {
            throw DomainException(BaseExceptions.UNAUTHORIZED)
        }

        val profile = profileService.findProfileWithIdAccount(account.id)

        return AccountVM.fromAccountAndProfile(
            accountService.updateAccount(account, updateProfileRequest.toAccount()),
            profileService.updateProfile(profile, updateProfileRequest.toProfile())
        )
    }
}
