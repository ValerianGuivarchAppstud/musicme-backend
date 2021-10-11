package com.vguivarc.musicme.backend.web.api.v1.contact.entities

import com.fasterxml.jackson.annotation.JsonProperty
import com.vguivarc.musicme.backend.domain.models.contact.Contact
import com.vguivarc.musicme.backend.domain.models.profile.Profile
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.ZonedDateTime

@ApiModel(value = "", description = "")
data class ContactVM(
    @JsonProperty("idProfile")
    @ApiModelProperty(value = "the profile id of the contact")
    val idProfile: String? = null,
    @JsonProperty("username")
    @ApiModelProperty(value = "the name of the contact")
    val username: String? = null,
    @JsonProperty("pictureUrl")
    @ApiModelProperty(value = "the picture of the contact")
    val pictureUrl: String? = null,
    @JsonProperty("createdAt")
    @ApiModelProperty(value = "the date the contact has been added")
    val createdAt: ZonedDateTime? = null,
    @JsonProperty("nbRingingSent")
    @ApiModelProperty(value = "the nb of sent ringing")
    val nbRingingSent: Int? = 0,
    @JsonProperty("nbRingingReceived")
    @ApiModelProperty(value = "the nb of received ringing")
    val nbRingingReceived: Int? = 0
) {
    companion object {
        fun fromContact(contact: Contact, profile: Profile, nbRingingSent: Int, nbRingingReceived: Int) = ContactVM(
            idProfile = profile.idProfile,
            username = profile.username,
            pictureUrl = profile.pictureUrl,
            createdAt = contact.createdDate,
            nbRingingSent = nbRingingSent,
            nbRingingReceived = nbRingingReceived
        )
    }
}
