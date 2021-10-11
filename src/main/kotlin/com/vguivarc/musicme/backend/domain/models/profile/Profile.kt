package com.vguivarc.musicme.backend.domain.models.profile

import com.vguivarc.musicme.backend.libraries.entities.Updatable
import java.time.ZonedDateTime

data class Profile(
    val idProfile: String = "",

    val idAccount: String = "",

    @Updatable
    val username: String? = null,

    @Updatable
    val pictureUrl: String? = null,

    @Updatable
    val dateLastSeen: ZonedDateTime? = ZonedDateTime.now(),

    val contactsId: MutableList<String> = mutableListOf()
)