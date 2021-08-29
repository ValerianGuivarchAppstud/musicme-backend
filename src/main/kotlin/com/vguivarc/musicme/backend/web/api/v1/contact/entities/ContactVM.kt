package com.vguivarc.musicme.backend.web.api.v1.contact.entities

import com.fasterxml.jackson.annotation.JsonProperty
import com.vguivarc.musicme.backend.domain.models.contact.Contact
import com.vguivarc.musicme.backend.domain.models.favorite.Favorite
import com.vguivarc.musicme.backend.domain.models.profile.Profile
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.ZonedDateTime

@ApiModel(value = "JWT access token", description = "the jwt access token")
data class ContactVM(
    @JsonProperty("title")
    @ApiModelProperty(value = "the nickname of the song")
    val nickname: String? = null,
    @JsonProperty("artworkUrl")
    @ApiModelProperty(value = "the pictureUrl of the contact")
    val pictureUrl: String? = null,
    @JsonProperty("createdAt")
    @ApiModelProperty(value = "the date the contact has been added")
    val createdAt: ZonedDateTime? = null
) {
    companion object {
        fun fromContact(contact: Contact, profile: Profile) = ContactVM(
            nickname = profile.nickname,
            pictureUrl = profile.pictureUrl,
            createdAt = contact.createdDate
        )
    }
}
