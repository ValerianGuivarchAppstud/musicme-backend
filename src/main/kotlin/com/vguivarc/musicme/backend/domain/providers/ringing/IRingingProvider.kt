package com.vguivarc.musicme.backend.domain.providers.ringing

import com.vguivarc.musicme.backend.domain.models.contact.Contact
import com.vguivarc.musicme.backend.domain.models.ringing.Ringing
import com.vguivarc.musicme.backend.domain.models.song.Song

interface IRingingProvider {
    fun sendRinging(contact: Contact, song: Song): Ringing

    fun listenRinging(contact: Contact, ringing: Ringing)

}