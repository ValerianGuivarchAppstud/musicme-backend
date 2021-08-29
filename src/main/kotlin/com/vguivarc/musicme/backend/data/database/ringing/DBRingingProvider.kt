package com.vguivarc.musicme.backend.data.database.ringing

import com.vguivarc.musicme.backend.domain.models.contact.Contact
import com.vguivarc.musicme.backend.domain.models.profile.Profile
import com.vguivarc.musicme.backend.domain.models.ringing.Ringing
import com.vguivarc.musicme.backend.domain.models.song.Song
import com.vguivarc.musicme.backend.domain.providers.favorite.IFavoriteProvider
import com.vguivarc.musicme.backend.domain.providers.favorite.responses.IFavoriteResponse
import com.vguivarc.musicme.backend.domain.providers.ringing.IRingingProvider
import com.vguivarc.musicme.backend.libraries.errors.DomainException
import com.vguivarc.musicme.backend.libraries.errors.ProviderExceptions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class DBRingingProvider : IRingingProvider {

//    @Autowired
  //  lateinit var repository: DBRingingRepository

    override fun sendRinging(contact: Contact, song: Song): Ringing {
        TODO("Not yet implemented")
    }

    override fun listenRinging(contact: Contact, ringing: Ringing) {
        TODO("Not yet implemented")
    }
}