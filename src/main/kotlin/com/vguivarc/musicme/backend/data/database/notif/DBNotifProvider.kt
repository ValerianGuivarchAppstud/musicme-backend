package com.vguivarc.musicme.backend.data.database.notif

import com.vguivarc.musicme.backend.domain.models.notif.Notif
import com.vguivarc.musicme.backend.domain.models.notif.NotifType
import com.vguivarc.musicme.backend.domain.providers.notif.INotifProvider
import com.vguivarc.musicme.backend.domain.providers.notif.responses.INotifResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DBNotifProvider : INotifProvider {

    @Autowired
    lateinit var repository: DBNotifRepository

    override fun sendNotif(idProfile: String, idProfileOfContact: String, type: NotifType): Notif {
        return repository.save(DBNotif(
            type = type,
            senderId = idProfile,
            receiverId = idProfileOfContact
        )).toNotif()
    }

    override fun getNotifList(id: String): List<INotifResponse> {
        return repository.findAllByReceiverId(id)
    }
}