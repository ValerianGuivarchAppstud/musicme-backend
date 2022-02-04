package com.vguivarc.musicme.backend.data.database.notif

import com.vguivarc.musicme.backend.domain.models.notif.Notif
import com.vguivarc.musicme.backend.domain.models.notif.NotifType
import com.vguivarc.musicme.backend.domain.models.ringing.Ringing
import com.vguivarc.musicme.backend.domain.models.ringing.RingingState
import com.vguivarc.musicme.backend.domain.models.song.Song
import com.vguivarc.musicme.backend.domain.providers.notif.responses.INotifResponse
import com.vguivarc.musicme.backend.domain.providers.ringing.responses.IRingingResponse
import com.vguivarc.musicme.backend.libraries.entities.Updatable
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.ZonedDateTime

@Document(collection = "notifs")
data class DBNotif(
    @Id
    var id: String? = null,

    @Field("createdDate")
    @CreatedDate
    var createdDate: ZonedDateTime = ZonedDateTime.now(),

    @Field("type")
    @Updatable
    val type: NotifType = NotifType.RINGING_RECEIVED,

    @Field("senderId")
    @Updatable
    val senderId: String = "",

    @Field("receiverId")
    @Updatable
    val receiverId: String = ""
) : INotifResponse {

    override fun toNotif(): Notif {
        return Notif(
            id = id ?: "",
            type = type,
            senderId = senderId,
            receiverId = receiverId
        )
    }
}
