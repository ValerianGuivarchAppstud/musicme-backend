package com.vguivarc.musicme.backend.domain.providers.account

import com.vguivarc.musicme.backend.domain.models.account.Account
import com.vguivarc.musicme.backend.domain.providers.account.responses.IAccountResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface IAccountProvider {

    fun create(toCreate: Account): IAccountResponse
    fun update(account: Account): IAccountResponse

    fun findOneById(id: String): IAccountResponse
    fun findOneByFacebookIdOrNull(id: String): IAccountResponse?
    fun findById(id: String): IAccountResponse?

    fun count(): Long

    fun existByFacebookId(facebookId: String): Boolean
    fun findByFacebookId(facebookId: String): IAccountResponse?

    fun findAll(p: Pageable): Page<IAccountResponse>
    fun findAllByEmailContains(email: String, p: Pageable): Page<IAccountResponse>
    fun findAllByStatusContains(status: String, p: Pageable): Page<IAccountResponse>
    fun findAllWithId(ids: List<String>, p: Pageable): Page<IAccountResponse>

    fun existByEmail(email: String): Boolean
    fun findByEmail(email: String): IAccountResponse?
    fun findOneByEmail(email: String): IAccountResponse

    fun findOrCreateAccountByEmail(email: String): IAccountResponse
    fun findListByFacebookId(facebookId: List<String>): List<IAccountResponse>
}
