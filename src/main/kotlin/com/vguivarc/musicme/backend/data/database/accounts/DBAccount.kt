package com.vguivarc.musicme.backend.data.database.accounts

import com.vguivarc.musicme.backend.domain.models.account.Account
import com.vguivarc.musicme.backend.domain.models.nested.AccountStatus
import com.vguivarc.musicme.backend.domain.providers.account.responses.IAccountResponse
import com.vguivarc.musicme.backend.libraries.entities.Updatable
import org.springframework.data.annotation.*
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.ZonedDateTime
import java.util.*

@Document(collection = "accounts")
data class DBAccount(

    @Id
    var id: String? = null,

    @Updatable
    @Field("email")
    var email: String? = null,

    @Field("facebookId")
    @Updatable
    var facebookId: String? = null,

    @Updatable
    @Field("password")
    var password: String? = null,

    @Updatable
    @Field("status")
    var status: AccountStatus = AccountStatus.NEW,

    @Field("secret")
    var secret: String? = null,

    @Field("uuid")
    var uuid: String = UUID.randomUUID().toString(),

    @CreatedBy
    @Field("createdBy")
    var createdBy: String? = null,

    @Field("createdDate")
    @CreatedDate
    var createdDate: ZonedDateTime = ZonedDateTime.now(),

    @LastModifiedBy
    @Field("lastModifiedBy")
    var lastModifiedBy: String? = null,

    @LastModifiedDate
    @Field("lastModifiedDate")
    var lastModifiedDate: ZonedDateTime = ZonedDateTime.now(),

    @Version
    @Field("version")
    var version: Long? = null
) : IAccountResponse {

    override fun toAccount(): Account {
        return Account(
            idAccount = id ?: "",
            password = password,
            uuid = uuid,
            email = email,
            facebookId = facebookId,
            secret = secret,
            status = status,
            createdDate = createdDate
        )
    }
}
