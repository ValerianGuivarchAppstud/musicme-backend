package com.vguivarc.musicme.backend.data.database.profile

import org.springframework.data.mongodb.repository.MongoRepository

interface DBProfileRepository : MongoRepository<DBProfile, String> {
    fun findOneById(id: String): DBProfile?
    fun findOneByIdAccount(id: String): DBProfile?
}
