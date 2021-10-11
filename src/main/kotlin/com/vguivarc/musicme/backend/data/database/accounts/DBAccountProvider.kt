package com.vguivarc.musicme.backend.data.database.accounts

import com.vguivarc.musicme.backend.domain.models.account.Account
import com.vguivarc.musicme.backend.domain.providers.account.IAccountProvider
import com.vguivarc.musicme.backend.domain.providers.account.responses.IAccountResponse
import com.vguivarc.musicme.backend.libraries.entities.EntityUtils
import com.vguivarc.musicme.backend.libraries.errors.DomainException
import com.vguivarc.musicme.backend.libraries.errors.ProviderExceptions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class DBAccountProvider : IAccountProvider {

    @Autowired
    lateinit var repository: DBAccountRepository

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    fun Account.toDBAccount(): DBAccount {
        return DBAccount(
            id = this.idAccount.ifBlank { null },
            email = email,
            status = status,
            secret = secret,
            facebookId = facebookId,
            password = password
        )
    }

    private fun generateSecret(): String {
        return UUID.randomUUID().toString() + UUID.randomUUID().toString()
    }

    override fun create(toCreate: Account): DBAccount {
        val accountToCreate = toCreate.toDBAccount()

        // check if account already exists
        toCreate.email?.let {
            repository.findOneByEmail(it)?.let {
                throw DomainException(ProviderExceptions.DB_ACCOUNT_ALREADY_EXISTS)
            }
        }

        accountToCreate.secret = generateSecret()
        if (!toCreate.password.isNullOrBlank()) {
            accountToCreate.password = passwordEncoder.encode(toCreate.password)
        }

        return repository.save(accountToCreate)
    }

    override fun update(account: Account): IAccountResponse {
        val baseAccount = repository.findOneById(
            account.idAccount
        ) ?: throw DomainException(ProviderExceptions.DB_ACCOUNT_NOT_FOUND)

        if (!account.password.isNullOrBlank() && baseAccount.password != account.password) {
            account.password = passwordEncoder.encode(account.password)
        }

        return repository.save(EntityUtils.update(baseAccount, account.toDBAccount()))
    }

    override fun findOneByIdAccount(idAccount: String): DBAccount {
        return repository.findOneById(
            idAccount
        ) ?: throw DomainException(ProviderExceptions.DB_ACCOUNT_NOT_FOUND)
    }

    override fun findOneByFacebookIdOrNull(idFacebook: String): DBAccount? {
        return repository.findOneByFacebookId(
            idFacebook
        )
    }

    override fun findByIdAccount(idAccount: String): DBAccount? {
        return repository.findOneById(idAccount)
    }

    override fun count(): Long {
        return repository.count()
    }

    override fun findAll(p: Pageable): Page<IAccountResponse> {
        return repository.findAll(p).map { it }
    }

    override fun existByFacebookId(facebookId: String): Boolean {
        return repository.findOneByFacebookId(facebookId) != null
    }

    override fun findByFacebookId(facebookId: String): DBAccount? {
        return repository.findOneByFacebookId(
            facebookId
        )
    }

    override fun existByEmail(email: String): Boolean {
        return repository.findOneByEmail(email) != null
    }

    override fun findByEmail(email: String): DBAccount? {
        return repository.findOneByEmail(email)
    }

    override fun findOneByEmail(email: String): DBAccount {
        return repository.findOneByEmail(email) ?: throw DomainException(ProviderExceptions.DB_ACCOUNT_NOT_FOUND)
    }

    override fun findOrCreateAccountByEmail(email: String): DBAccount {
        val account = repository.findOneByEmail(email)
            ?: DBAccount(
                email = email,
                secret = generateSecret()
            )
        return repository.save(account)
    }

    override fun findListByFacebookId(facebookId: List<String>): List<DBAccount> {
        return facebookId.mapNotNull { repository.findOneByFacebookId(it) }
    }
}
