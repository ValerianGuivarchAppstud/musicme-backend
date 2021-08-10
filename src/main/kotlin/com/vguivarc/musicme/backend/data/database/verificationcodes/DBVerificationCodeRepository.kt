package com.vguivarc.musicme.backend.data.database.verificationcodes

import org.springframework.data.mongodb.repository.MongoRepository

interface DBVerificationCodeRepository : MongoRepository<DBVerificationCode, String> {

    fun findByAccountId(accountId: String): List<DBVerificationCode>

    fun findOneById(id: String): DBVerificationCode
}
