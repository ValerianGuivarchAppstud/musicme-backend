package com.vguivarc.musicme.backend.domain.services

import com.vguivarc.musicme.backend.domain.models.account.Account
import com.vguivarc.musicme.backend.domain.models.nested.AccountStatus
import com.vguivarc.musicme.backend.domain.providers.account.IAccountProvider
import com.vguivarc.musicme.backend.libraries.entities.EntityUtils
import com.vguivarc.musicme.backend.libraries.errors.DomainException
import com.vguivarc.musicme.backend.libraries.errors.DomainExceptions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class AccountService {

    @Autowired
    lateinit var accountProvider: IAccountProvider

    fun existsByFacebookId(facebookId: String): Boolean {
        return accountProvider.existByFacebookId(facebookId)
    }

    fun existByEmail(email: String): Boolean {
        return accountProvider.existByEmail(email)
    }

    fun createAccount(account: Account): Account {
        return accountProvider.create(account).toAccount()
    }

    fun updateAccount(account: Account, accountUpdate: Account): Account {
        if (account.email != accountUpdate.email && accountProvider.existByEmail("${accountUpdate.email}")) {
            throw DomainException(DomainExceptions.ACCOUNT_EMAIL_ALREADY_REGISTERED)
        }
        return accountProvider.update(
            EntityUtils.update(account, accountUpdate).copy(password = accountUpdate.password)
        ) // avoid encoding previously encoded password
            .toAccount()
    }

    fun validateAccount(account: Account): Account {
        return accountProvider.update(account.copy(status = AccountStatus.ACTIVE)).toAccount()
    }
}
