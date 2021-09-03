package com.vguivarc.musicme.backend.data.database.favorite

import com.vguivarc.musicme.backend.domain.models.favorite.Favorite
import com.vguivarc.musicme.backend.domain.models.profile.Profile
import com.vguivarc.musicme.backend.domain.models.song.Song
import com.vguivarc.musicme.backend.domain.providers.favorite.responses.IFavoriteResponse
import com.vguivarc.musicme.backend.domain.providers.profile.responses.IProfileResponse
import com.vguivarc.musicme.backend.libraries.entities.Updatable
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.ZonedDateTime

@Document(collection = "favorites")
data class DBFavorite(

    @Id
    var id: String? = null,

    @Field("idOwner")
    var idOwner: String = "",

    @Field("idSong")
    var idSong: String = "",

    @Updatable
    val title: String = "",

    @Field("artworkUrl")
    @Updatable
    val artworkUrl: String = "",

    @Field("createdDate")
    @CreatedDate
    var createdDate: ZonedDateTime = ZonedDateTime.now()

) : IFavoriteResponse {

    override fun toFavorite(): Favorite {
        return Favorite(
            createdDate = createdDate,
            idOwner = idOwner,
            song = Song(idSong, title, artworkUrl)
        )
    }
}
