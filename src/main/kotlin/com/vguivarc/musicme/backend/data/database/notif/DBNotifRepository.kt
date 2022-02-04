package com.vguivarc.musicme.backend.data.database.notif

import com.vguivarc.musicme.backend.domain.providers.notif.responses.INotifResponse
import org.springframework.data.mongodb.repository.MongoRepository

interface DBNotifRepository : MongoRepository<DBNotif, String> {

    fun findAllByReceiverId(receiverId: String): List<INotifResponse>
    fun findAllBySenderIdAndReceiverId(senderId: String, receiverId: String): List<INotifResponse>
    fun findOneById(ringingId: String): DBNotif?

}
