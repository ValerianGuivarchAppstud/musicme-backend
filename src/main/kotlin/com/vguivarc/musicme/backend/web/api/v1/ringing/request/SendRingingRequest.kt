package com.vguivarc.musicme.backend.web.api.v1.ringing.request

import com.vguivarc.musicme.backend.domain.models.song.Song
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.NotNull

@ApiModel(description = "the request to update the user profile")
data class SendRingingRequest(
    @field:NotNull
    @ApiModelProperty(value = "the song", allowEmptyValue = true)
    val song: Song,
    @field:NotNull
    @ApiModelProperty(value = "the contact", allowEmptyValue = true)
    val idProfileOfContact: String
)
