package com.vguivarc.musicme.backend.web.api.v1.favorite.entities

import com.fasterxml.jackson.annotation.JsonProperty
import com.vguivarc.musicme.backend.domain.models.favorite.Favorite
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.ZonedDateTime

@ApiModel(value = "JWT access token", description = "the jwt access token")
data class FavoriteVM(
    @JsonProperty("title")
    @ApiModelProperty(value = "the title of the song")
    val title: String? = null,
    @JsonProperty("artworkUrl")
    @ApiModelProperty(value = "the artworkUrl of the song")
    val artworkUrl: String? = null,
    @JsonProperty("createdAt")
    @ApiModelProperty(value = "the date the favorite has been added")
    val createdAt: ZonedDateTime? = null
) {
    companion object {
        fun fromFavorite(favorite: Favorite) = FavoriteVM(
            title = favorite.song.title,
            artworkUrl = favorite.song.artworkUrl,
            createdAt = favorite.createdDate
        )
    }
}
