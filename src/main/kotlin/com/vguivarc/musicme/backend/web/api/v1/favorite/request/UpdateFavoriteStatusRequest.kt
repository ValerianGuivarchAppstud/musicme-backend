package com.vguivarc.musicme.backend.web.api.v1.favorite.request

import com.vguivarc.musicme.backend.domain.models.song.Song
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.NotNull

@ApiModel(description = "the request to update the user profile")
data class UpdateFavoriteStatusRequest(
    @field:NotNull
    @ApiModelProperty(value = "the song", allowEmptyValue = true)
    val song: Song,
    @field:NotNull
    @ApiModelProperty(value = "the favorite status", allowEmptyValue = true)
    val status: Boolean
)
