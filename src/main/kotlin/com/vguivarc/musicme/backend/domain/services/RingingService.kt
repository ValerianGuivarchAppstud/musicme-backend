package com.vguivarc.musicme.backend.domain.services

import com.vguivarc.musicme.backend.domain.models.contact.Contact
import com.vguivarc.musicme.backend.domain.models.favorite.Favorite
import com.vguivarc.musicme.backend.domain.models.profile.Profile
import com.vguivarc.musicme.backend.domain.models.ringing.Ringing
import com.vguivarc.musicme.backend.domain.models.song.Song
import com.vguivarc.musicme.backend.domain.providers.favorite.IFavoriteProvider
import com.vguivarc.musicme.backend.domain.providers.profile.IProfileProvider
import com.vguivarc.musicme.backend.domain.providers.ringing.IRingingProvider
import com.vguivarc.musicme.backend.libraries.entities.EntityUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RingingService {

    @Autowired
    lateinit var ringingProvider: IRingingProvider


    fun sendRinging(contact: Contact, song: Song): Ringing {
        return this.ringingProvider.sendRinging(contact, song)
    }


    fun listenRinging(contact: Contact, ringing: Ringing) {
        this.ringingProvider.listenRinging(contact, ringing)
    }
}
