package com.vguivarc.musicme.backend.data.database.accounts

import com.vguivarc.musicme.backend.domain.providers.account.responses.IAccountResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository

interface DBAccountRepository : MongoRepository<DBAccount, String> {

    fun findOneByEmail(email: String): DBAccount?
    fun findOneByFacebookId(facebookId: String): DBAccount?
    fun findOneById(id: String): DBAccount?

    fun findAllByEmailContains(email: String, p: Pageable): Page<IAccountResponse>
    fun findAllByIdIn(ids: List<String>, p: Pageable): Page<IAccountResponse>
    fun findAllByStatusContains(status: String, p: Pageable): Page<IAccountResponse>
}
