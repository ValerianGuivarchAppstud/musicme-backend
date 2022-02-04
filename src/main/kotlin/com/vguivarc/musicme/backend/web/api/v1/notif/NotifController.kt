package com.vguivarc.musicme.backend.web.api.v1.notif

import com.vguivarc.musicme.backend.domain.services.AccountService
import com.vguivarc.musicme.backend.domain.services.AuthenticationService
import com.vguivarc.musicme.backend.domain.services.NotifService
import com.vguivarc.musicme.backend.domain.services.ProfileService
import com.vguivarc.musicme.backend.web.api.v1.notif.entities.NotifVM
import io.swagger.annotations.Api
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/v1/notif")
@RestController
@Api(
    value = "favorite",
    description = "handle the favorite requests"
)
class NotifController {

    @Autowired
    lateinit var authenticationService: AuthenticationService

    @Autowired
    lateinit var accountService: AccountService

    @Autowired
    lateinit var profileService: ProfileService

    @Autowired
    lateinit var notifService: NotifService

    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    fun getNotifList(): List<NotifVM> {
        val account = authenticationService.findConnectedAccountOrThrowAccessDenied()

        val profileOwner = profileService.findProfileWithIdAccount(account.idAccount)

        val notifList = notifService.getNotifList(profileOwner.idProfile)

        return notifList.map {
            val profileSender = profileService.findProfileWithIdProfile(it.toNotif().senderId)
            val profileReceiver = profileService.findProfileWithIdProfile(it.toNotif().senderId)
            NotifVM.fromNotif(it.toNotif(), profileSender, profileReceiver)
        }
    }
}
