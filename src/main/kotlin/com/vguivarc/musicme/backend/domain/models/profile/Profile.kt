package com.vguivarc.musicme.backend.domain.models.profile

import com.vguivarc.musicme.backend.libraries.entities.Updatable

data class Profile(
    val id: String = "",
    val idAccount: String = "",
    @Updatable
    val firstName: String? = null,
    @Updatable
    val lastName: String? = null
)
