package com.vguivarc.musicme.backend.web.api.v1.contact

import com.vguivarc.musicme.backend.domain.services.*
import com.vguivarc.musicme.backend.web.api.v1.contact.entities.ContactFacebookVM
import com.vguivarc.musicme.backend.web.api.v1.contact.entities.ContactVM
import com.vguivarc.musicme.backend.web.api.v1.contact.request.UpdateContactStatusRequest
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RequestMapping("/api/v1/contact")
@RestController
@Api(
    value = "contact",
    description = "handle the favorite requests"
)
class ContactController {

    @Autowired
    lateinit var authenticationService: AuthenticationService

    @Autowired
    lateinit var ringingService: RingingService

    @Autowired
    lateinit var profileService: ProfileService

    @Autowired
    lateinit var contactService: ContactService

    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "get the user contacts list")
    fun getContactList(): List<ContactVM> {
        val account = authenticationService.findConnectedAccountOrThrowAccessDenied()

        val profile = profileService.findProfileWithIdAccount(account.idAccount)

        val list = contactService.getContacts(profile)

        return list.map { ContactVM.fromContact(
            it,
            profileService.findProfileWithIdProfile(it.idProfileOfContact),
            ringingService.getNbRingingSent(it),
            ringingService.getNbRingingReceived(it)
        ) }
    }

    @GetMapping("/facebook")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "get the user contacts list")
    fun getContactFacebookList(
        @RequestParam("socialAuthToken")
        socialAuthToken: String
    ): List<ContactFacebookVM> {

        val account = authenticationService.findConnectedAccountOrThrowAccessDenied()

        val profile = profileService.findProfileWithIdAccount(account.idAccount)

        val contactFacebookIdList = contactService.getFacebookContactsId(socialAuthToken)

        val contactList = contactService.getContacts(profile)

        val accountList = authenticationService.findListByFacebookId(contactFacebookIdList)

        val profileList = profileService.findListByIdAccount(accountList)

        val res = profileList.map { pr -> ContactFacebookVM.fromContact(
            contactList.filter { pr.idProfile == it.idProfileOfContact }.getOrNull(0),
            accountList.filter { pr.idAccount == it.idAccount }[0],
            pr
        ) }
        return res
    }

    @PutMapping("/status")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "add or remove a contact")
    fun updateContactStatus(
        @Valid
        @RequestBody
        @ApiParam(value = "the request to add or remove a contact", required = true)
        updateContactStatusRequest: UpdateContactStatusRequest
    ) : List<ContactVM>{
        val account = authenticationService.findConnectedAccountOrThrowAccessDenied()

        val profile = profileService.findProfileWithIdAccount(account.idAccount)

        contactService.saveContactStatus(
            profile.idProfile,
            updateContactStatusRequest.profileContactId,
            updateContactStatusRequest.status
        )

        return getContactList()
    }
}
