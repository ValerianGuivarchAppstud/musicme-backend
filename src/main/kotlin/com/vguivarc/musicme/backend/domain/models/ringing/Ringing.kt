package com.vguivarc.musicme.backend.domain.models.ringing

import com.vguivarc.musicme.backend.domain.models.song.Song
import java.time.ZonedDateTime

data class Ringing (
    val id: String = "",
    val createdDate: ZonedDateTime = ZonedDateTime.now(),
    val song: Song? = null,
    val state: RingingState = RingingState.WAITING,
    val senderId: String = "",
    val receiverId: String = ""
)