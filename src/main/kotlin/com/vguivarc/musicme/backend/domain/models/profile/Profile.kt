package com.vguivarc.musicme.backend.domain.models.profile

import com.vguivarc.musicme.backend.libraries.entities.Updatable

data class Profile(
    val id: String = "",
    val idAccount: String = "",

    @Updatable
    val nickname: String? = null,

    @Updatable
    val pictureUrl: String? = null,

    val contactsId: MutableList<String> = mutableListOf()
)
