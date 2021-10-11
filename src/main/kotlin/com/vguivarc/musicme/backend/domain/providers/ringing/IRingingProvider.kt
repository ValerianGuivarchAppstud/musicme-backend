package com.vguivarc.musicme.backend.domain.providers.ringing

import com.vguivarc.musicme.backend.domain.models.contact.Contact
import com.vguivarc.musicme.backend.domain.models.ringing.Ringing
import com.vguivarc.musicme.backend.domain.models.song.Song
import com.vguivarc.musicme.backend.domain.providers.ringing.responses.IRingingResponse

interface IRingingProvider {
    fun sendRinging(idProfile: String, idProfileOfContact: String, song: Song): Ringing

    fun listenRinging(ringingId: String)

    fun getNextRinging(id: String): Ringing?

    fun getWaitingRingingList(id: String): List<IRingingResponse>

    fun getListRingingListened(id: String): List<Ringing>

    fun getNbRingingSent(contact: Contact): Int

    fun getNbRingingReceived(contact: Contact): Int
}