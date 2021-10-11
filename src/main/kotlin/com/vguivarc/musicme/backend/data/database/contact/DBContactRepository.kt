package com.vguivarc.musicme.backend.data.database.contact

import com.vguivarc.musicme.backend.domain.providers.contact.responses.IContactResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository

interface DBContactRepository : MongoRepository<DBContact, String> {

    fun findAllByIdProfile(idProfile: String): List<IContactResponse>
    fun findOneByIdProfile(idProfile: String, idProfileOfContact: String): DBContact?

}
