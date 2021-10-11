package com.vguivarc.musicme.backend.domain.models.song

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@ApiModel(description = "the song description")
data class Song(
    @field:NotNull
    @field:NotBlank
    @ApiModelProperty(value = "the song id", allowEmptyValue = true)
    val id: String = "",
    @field:NotNull
    @field:NotBlank
    @ApiModelProperty(value = "the song title", allowEmptyValue = true)
    val title: String = "",
    @field:NotNull
    @field:NotBlank
    @ApiModelProperty(value = "the song picture url", allowEmptyValue = true)
    val pictureUrl: String = ""
)