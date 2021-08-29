package com.vguivarc.musicme.backend.web.api.v1.contact.request

import com.vguivarc.musicme.backend.domain.models.account.Account
import com.vguivarc.musicme.backend.domain.models.contact.Contact
import com.vguivarc.musicme.backend.domain.models.favorite.Favorite
import com.vguivarc.musicme.backend.domain.models.profile.Profile
import com.vguivarc.musicme.backend.domain.models.song.Song
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import jdk.jfr.BooleanFlag
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@ApiModel(description = "the request to update the user profile")
data class UpdateContactStatusRequest(
    @field:NotNull
    @field:NotBlank
    @ApiModelProperty(value = "the id contact", allowEmptyValue = true)
    val idContact: String,
    @field:NotNull
    @ApiModelProperty(value = "the contact status", allowEmptyValue = true)
    val status: Boolean
)
