package com.vguivarc.musicme.backend.domain.services

import com.vguivarc.musicme.backend.domain.models.contact.Contact
import com.vguivarc.musicme.backend.domain.models.notif.Notif
import com.vguivarc.musicme.backend.domain.models.ringing.Ringing
import com.vguivarc.musicme.backend.domain.models.song.Song
import com.vguivarc.musicme.backend.domain.providers.notif.INotifProvider
import com.vguivarc.musicme.backend.domain.providers.notif.responses.INotifResponse
import com.vguivarc.musicme.backend.domain.providers.ringing.IRingingProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class NotifService {

    @Autowired
    lateinit var notifProvider: INotifProvider

    fun getNotifList(id: String): List<INotifResponse> {
        return this.notifProvider.getNotifList(id)
    }
}
