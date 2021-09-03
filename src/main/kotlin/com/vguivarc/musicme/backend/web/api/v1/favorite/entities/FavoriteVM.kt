package com.vguivarc.musicme.backend.web.api.v1.favorite.entities

import com.fasterxml.jackson.annotation.JsonProperty
import com.vguivarc.musicme.backend.domain.models.favorite.Favorite
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.ZonedDateTime

@ApiModel(value = "JWT access token", description = "the jwt access token")
data class FavoriteVM(
    @JsonProperty("id")
    @ApiModelProperty(value = "the id of the song")
    val id: String? = null,
    @JsonProperty("title")
    @ApiModelProperty(value = "the title of the song")
    val title: String? = null,
    @JsonProperty("pictureUrl")
    @ApiModelProperty(value = "the artworkUrl of the song")
    val pictureUrl: String? = null,
    @JsonProperty("createdAt")
    @ApiModelProperty(value = "the date the favorite has been added")
    val createdAt: ZonedDateTime? = null
) {
    companion object {
        fun fromFavorite(favorite: Favorite) = FavoriteVM(
            id = favorite.song.id,
            title = favorite.song.title,
            pictureUrl = favorite.song.artworkUrl,
            createdAt = favorite.createdDate
        )
    }
}
