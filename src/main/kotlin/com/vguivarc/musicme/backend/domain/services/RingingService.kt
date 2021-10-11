package com.vguivarc.musicme.backend.domain.services

import com.vguivarc.musicme.backend.domain.models.contact.Contact
import com.vguivarc.musicme.backend.domain.models.profile.Profile
import com.vguivarc.musicme.backend.domain.models.ringing.Ringing
import com.vguivarc.musicme.backend.domain.models.song.Song
import com.vguivarc.musicme.backend.domain.providers.ringing.IRingingProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RingingService {

    @Autowired
    lateinit var ringingProvider: IRingingProvider


    fun sendRinging(idProfile: String, idProfileOfContact: String, song: Song): Ringing {
        return this.ringingProvider.sendRinging(idProfile, idProfileOfContact, song)
    }

    fun getNextRinging(id: String): Ringing? {
        return this.ringingProvider.getNextRinging(id)
    }

    fun getWaitingRingingList(id: String): List<Ringing> {
        return this.ringingProvider.getWaitingRingingList(id).map { it.toRinging() }
    }

    fun getListRingingListened(id: String): List<Ringing> {
        return this.ringingProvider.getListRingingListened(id)
    }


    fun listenRinging(ringingId: String) {
        this.ringingProvider.listenRinging(ringingId)
    }

    fun getNbRingingSent(contact: Contact): Int {
        return this.ringingProvider.getNbRingingSent(contact)
    }

    fun getNbRingingReceived(contact: Contact): Int {
        return this.ringingProvider.getNbRingingReceived(contact)

    }
}
