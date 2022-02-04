package com.vguivarc.musicme.backend.web.api.v1.contact.entities

import com.fasterxml.jackson.annotation.JsonProperty
import com.vguivarc.musicme.backend.domain.models.profile.Profile
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel(value = "", description = "")
data class SearchResultContactVM(
    @JsonProperty("foundResult")
    @ApiModelProperty(value = "the profile id of the user")
    val foundResult: Boolean = false,
    @JsonProperty("idProfile")
    @ApiModelProperty(value = "the profile id of the user")
    val idProfile: String? = null,
    @JsonProperty("username")
    @ApiModelProperty(value = "the name of the user")
    val username: String? = null,
    @JsonProperty("pictureUrl")
    @ApiModelProperty(value = "the picture of the user")
    val pictureUrl: String? = null,
    @JsonProperty("contact")
    @ApiModelProperty(value = "is the user a contact?")
    val contact: Boolean? = null
) {
    companion object {
        fun fromProfile(profile: Profile?, contact: Boolean?) = SearchResultContactVM(
            foundResult = profile!=null,
            idProfile = profile?.idProfile,
            username = profile?.username,
            pictureUrl = profile?.pictureUrl,
            contact = contact
        )
    }
}
