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
    @JsonProperty("idProfile")
    @ApiModelProperty(value = "the id of the contact")
    val idProfile: String? = null,
    @JsonProperty("idFacebook")
    @ApiModelProperty(value = "the facebook id of the contact")
    val idFacebook: String? = null,
    @JsonProperty("username")
    @ApiModelProperty(value = "the name of the contact")
    val username: String? = null,
    @JsonProperty("picture")
    @ApiModelProperty(value = "the picture of the contact")
    val picture: String? = null,
    @JsonProperty("contact")
    @ApiModelProperty(value = "is a contact?")
    val contact: Boolean? = null,
    @JsonProperty("createdAt")
    @ApiModelProperty(value = "the date the contact has been added")
    val createdAt: ZonedDateTime? = null
) {
    companion object {
        fun fromContact(contact: Contact?, account: Account, profile: Profile) = ContactFacebookVM(
            idProfile = profile.idProfile,
            idFacebook = account.facebookId,
            username = profile.username,
            picture = profile.pictureUrl,
            contact = (contact!=null),
            createdAt = contact?.createdDate
        )
    }
}
