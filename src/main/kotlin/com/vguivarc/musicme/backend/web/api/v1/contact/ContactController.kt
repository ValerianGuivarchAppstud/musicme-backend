package com.vguivarc.musicme.backend.web.api.v1.contact

import com.vguivarc.musicme.backend.domain.models.favorite.Favorite
import com.vguivarc.musicme.backend.domain.services.*
import com.vguivarc.musicme.backend.web.api.v1.contact.entities.ContactVM
import com.vguivarc.musicme.backend.web.api.v1.contact.request.UpdateContactStatusRequest
import com.vguivarc.musicme.backend.web.api.v1.favorite.entities.FavoriteVM
import com.vguivarc.musicme.backend.web.api.v1.favorite.request.UpdateFavoriteStatusRequest
import com.vguivarc.musicme.backend.web.api.v1.user.AccountVM
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RequestMapping("/api/v1/contacts")
@RestController
@Api(
    value = "contact",
    description = "handle the favorite requests"
)
class ContactController {

    @Autowired
    lateinit var authenticationService: AuthenticationService

    @Autowired
    lateinit var accountService: AccountService

    @Autowired
    lateinit var profileService: ProfileService

    @Autowired
    lateinit var contactService: ContactService

    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "get the user contacts list")
    fun getContactList(): List<ContactVM> {
        val account = authenticationService.findConnectedAccountOrThrowAccessDenied()

        val profile = profileService.findProfileWithIdAccount(account.id)

        val list = contactService.getContacts(profile)

        return list.map { ContactVM.fromContact(
            it,
            profileService.findProfileWithIdAccount(it.idProfileOfContact)
        ) }
    }

    @PostMapping("status")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "add or remove a contact")
    fun updateContactStatus(
        @Valid
        @RequestBody
        @ApiParam(value = "the request to add or remove a contact", required = true)
        updateContactStatusRequest: UpdateContactStatusRequest
    ) : List<ContactVM>{
        val account = authenticationService.findConnectedAccountOrThrowAccessDenied()

        contactService.saveContactStatus(
            account.id,
            updateContactStatusRequest.idContact,
            updateContactStatusRequest.status
        )

        return getContactList()
    }
}
