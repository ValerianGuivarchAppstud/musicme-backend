package com.vguivarc.musicme.backend.web.api.v1.user.profile

import com.vguivarc.musicme.backend.domain.services.AccountService
import com.vguivarc.musicme.backend.domain.services.AuthenticationService
import com.vguivarc.musicme.backend.domain.services.ProfileService
import com.vguivarc.musicme.backend.web.api.v1.user.profile.requests.UpdateProfileRequest
import com.vguivarc.musicme.backend.web.api.v1.user.AccountVM
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RequestMapping("/api/v1/user/profile")
@RestController
@Api(
    value = "user profile",
    description = "handle the users profile requests"
)
class UserProfileController {

    @Autowired
    lateinit var authenticationService: AuthenticationService

    @Autowired
    lateinit var accountService: AccountService

    @Autowired
    lateinit var profileService: ProfileService

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "get the user profile")
    fun getAccount(): AccountVM {
        val account = authenticationService.findConnectedAccountOrThrowAccessDenied()

        val profile = profileService.findProfileWithIdAccount(account.id)

        return AccountVM.fromAccountAndProfile(account, profile)
    }

    @PutMapping("")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "updates the user profile")
    fun updateProfile(
        @Valid
        @RequestBody
        @ApiParam(value = "the request to update the profile", required = true)
        updateProfileRequest: UpdateProfileRequest
    ): AccountVM {
        val account = authenticationService.findConnectedAccountOrThrowAccessDenied()

        val profile = profileService.findProfileWithIdAccount(account.id)

        return AccountVM.fromAccountAndProfile(
            accountService.updateAccount(account, updateProfileRequest.toAccount()),
            profileService.updateProfile(profile, updateProfileRequest.toProfile())
        )
    }
}
