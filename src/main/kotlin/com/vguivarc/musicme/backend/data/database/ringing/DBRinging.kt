package com.vguivarc.musicme.backend.data.database.ringing

import com.vguivarc.musicme.backend.domain.models.ringing.Ringing
import com.vguivarc.musicme.backend.domain.models.ringing.RingingState
import com.vguivarc.musicme.backend.domain.models.song.Song
import com.vguivarc.musicme.backend.domain.providers.ringing.responses.IRingingResponse
import com.vguivarc.musicme.backend.libraries.entities.Updatable
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.ZonedDateTime

@Document(collection = "ringings")
data class DBRinging(
    @Id
    var id: String? = null,

    @Field("createdDate")
    @CreatedDate
    var createdDate: ZonedDateTime = ZonedDateTime.now(),

    @Field("song")
    @Updatable
    val song: Song? = null,

    @Field("state")
    @Updatable
    val state: RingingState = RingingState.WAITING,

    @Field("senderId")
    @Updatable
    val senderId: String = "",

    @Field("receiverId")
    @Updatable
    val receiverId: String = ""
) : IRingingResponse {

    override fun toRinging(): Ringing {
        return Ringing(
            id = id ?: "",
            createdDate = createdDate,
            song = song,
            state = state,
            senderId = senderId,
            receiverId = receiverId
        )
    }
}
