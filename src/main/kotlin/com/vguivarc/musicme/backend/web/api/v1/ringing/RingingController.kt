package com.vguivarc.musicme.backend.web.api.v1.ringing

import com.vguivarc.musicme.backend.domain.services.*
import com.vguivarc.musicme.backend.web.api.v1.ringing.entities.NextRingingResponseVM
import com.vguivarc.musicme.backend.web.api.v1.ringing.entities.RingingVM
import com.vguivarc.musicme.backend.web.api.v1.ringing.request.SendRingingRequest
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RequestMapping("/api/v1/ringing")
@RestController
@Api(
    value = "favorite",
    description = "handle the favorite requests"
)
class RingingController {

    @Autowired
    lateinit var authenticationService: AuthenticationService

    @Autowired
    lateinit var accountService: AccountService

    @Autowired
    lateinit var profileService: ProfileService

    @Autowired
    lateinit var ringingService: RingingService

    @GetMapping("/next")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "get the next ringing")
    fun getNextRinging(): NextRingingResponseVM {
        val account = authenticationService.findConnectedAccountOrThrowAccessDenied()

        val profileOwner = profileService.findProfileWithIdAccount(account.idAccount)

        val ringing = ringingService.getNextRinging(profileOwner.idProfile)

        val ringingVM = ringing?.let {
            val profileSender = profileService.findProfileWithIdProfile(it.senderId)
            val profileReceiver = profileService.findProfileWithIdProfile(it.senderId)
            RingingVM.fromRinging(ringing, profileSender, profileReceiver)
        }
        return NextRingingResponseVM(nextRinging = ringingVM)
    }

    @GetMapping("/list/waiting")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "get the ringing list")
    fun getWaitingRingingList(): List<RingingVM> {
        val account = authenticationService.findConnectedAccountOrThrowAccessDenied()

        val profileOwner = profileService.findProfileWithIdAccount(account.idAccount)

        val ringingList = ringingService.getWaitingRingingList(profileOwner.idProfile)

        return ringingList.map {

            val profileSender = profileService.findProfileWithIdProfile(it.senderId)
            val profileReceiver = profileService.findProfileWithIdProfile(it.senderId)
            RingingVM.fromRinging(it, profileSender, profileReceiver)
        }
    }

    @GetMapping("/list/listened")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "get the ringing list")
    fun getListRingingListened(): List<RingingVM> {
        val account = authenticationService.findConnectedAccountOrThrowAccessDenied()

        val profileOwner = profileService.findProfileWithIdAccount(account.idAccount)

        val ringingList = ringingService.getListRingingListened(profileOwner.idProfile)

        return ringingList.map {

            val profileSender = profileService.findProfileWithIdProfile(it.senderId)
            val profileReceiver = profileService.findProfileWithIdProfile(it.senderId)
            RingingVM.fromRinging(it, profileSender, profileReceiver)
        }
    }

    @PutMapping("/listen")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "put the ringing as listened")
    fun listenRinging(
        @Valid
        @RequestBody
        @ApiParam(value = "the listened ringing", required = true)
        ringingId: String
    ) {
        authenticationService.findConnectedAccountOrThrowAccessDenied()
        ringingService.listenRinging(ringingId)
    }

    @PostMapping("/send")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "put the ringing as listened")
    fun sendRinging(
        @Valid
        @RequestBody
        @ApiParam(value = "the listened ringing", required = true)
        sendRingingRequest: SendRingingRequest
    ) : RingingVM{
        val account = authenticationService.findConnectedAccountOrThrowAccessDenied()

        val profileSender = profileService.findProfileWithIdAccount(account.idAccount)

        val profileReceiver = profileService.findProfileWithIdProfile(sendRingingRequest.idProfileOfContact)

        val ringing = ringingService.sendRinging(profileSender.idProfile, sendRingingRequest.idProfileOfContact, sendRingingRequest.song)

        return RingingVM.fromRinging(ringing, profileSender, profileReceiver)
    }
}
