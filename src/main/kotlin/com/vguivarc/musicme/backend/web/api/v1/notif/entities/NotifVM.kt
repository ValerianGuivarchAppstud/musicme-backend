package com.vguivarc.musicme.backend.web.api.v1.notif.entities

import com.fasterxml.jackson.annotation.JsonProperty
import com.vguivarc.musicme.backend.domain.models.notif.Notif
import com.vguivarc.musicme.backend.domain.models.notif.NotifType
import com.vguivarc.musicme.backend.domain.models.profile.Profile
import java.time.ZonedDateTime


data class NotifVM(
    @JsonProperty("id")
    val id: String? = null,
    @JsonProperty("song")
    val type: NotifType? = null,
    @JsonProperty("dateSent")
    val dateSent: ZonedDateTime? = null,
    @JsonProperty("seen")
    val seen: Boolean? = false,
    @JsonProperty("receiverId")
    val receiverId: String? = "",
    @JsonProperty("senderId")
    val senderId: String? = "",
    @JsonProperty("senderName")
    val senderName: String? = "",
    @JsonProperty("senderPictureUrl")
    val senderPictureUrl: String? = ""
) {
    companion object {
        fun fromNotif(notif: Notif, sender: Profile, receiver: Profile) = NotifVM(
            id = notif.id,
            type = notif.type,
            dateSent = notif.createdDate,
            seen = notif.createdDate < receiver.dateLastSeen,
            receiverId = notif.receiverId,
            senderId = notif.senderId,
            senderName = sender.username,
            senderPictureUrl = sender.pictureUrl
        )
    }
}
