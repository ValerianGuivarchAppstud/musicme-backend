package com.vguivarc.musicme.backend.domain.models.favorite

import com.vguivarc.musicme.backend.domain.models.song.Song
import com.vguivarc.musicme.backend.libraries.entities.Updatable
import java.time.ZonedDateTime

data class Favorite (
    val createdDate: ZonedDateTime = ZonedDateTime.now(),

    //TODO pkoi valeur par défaut ?
    @Updatable
    val song: Song,

    val idOwner : String = ""
)