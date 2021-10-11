package com.vguivarc.musicme.backend.data.database.ringing

import com.vguivarc.musicme.backend.domain.providers.ringing.responses.IRingingResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository

interface DBRingingRepository : MongoRepository<DBRinging, String> {

    fun findAllBySenderIdAndReceiverId(senderId: String, receiverId: String): List<IRingingResponse>
    fun findAllByReceiverId(senderId: String): List<IRingingResponse>
    fun findOneById(ringingId: String): DBRinging?

}
