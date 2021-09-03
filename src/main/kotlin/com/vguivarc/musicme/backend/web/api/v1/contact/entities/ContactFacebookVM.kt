package com.vguivarc.musicme.backend.web.api.v1.contact.entities

import com.fasterxml.jackson.annotation.JsonProperty
import com.vguivarc.musicme.backend.domain.models.account.Account
import com.vguivarc.musicme.backend.domain.models.contact.Contact
import com.vguivarc.musicme.backend.domain.models.profile.Profile
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.ZonedDateTime

@ApiModel(value = "JWT access token", description = "the jwt access token")
data class ContactFacebookVM(
    @JsonProperty("id")
    @ApiModelProperty(value = "the id of the contact")
    val id: String? = null,
    @JsonProperty("idFacebook")
    @ApiModelProperty(value = "the facebook id of the contact")
    val idFacebook: String? = null,
    @JsonProperty("name")
    @ApiModelProperty(value = "the name of the contact")
    val name: String? = null,
    @JsonProperty("picture")
    @ApiModelProperty(value = "the picture of the contact")
    val picture: String? = null,
    @JsonProperty("isContact")
    @ApiModelProperty(value = "is a contact?")
    val isContact: Boolean? = null,
    @JsonProperty("createdAt")
    @ApiModelProperty(value = "the date the contact has been added")
    val createdAt: ZonedDateTime? = null
) {
    companion object {
        fun fromContact(contact: Contact?, account: Account, profile: Profile) = ContactFacebookVM(
            id = profile.id,
            idFacebook = account.facebookId,
            name = profile.username,
            picture = profile.pictureUrl,
            isContact = (contact!=null),
            createdAt = contact?.createdDate
        )
    }
}
