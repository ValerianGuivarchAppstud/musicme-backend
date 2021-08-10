package com.vguivarc.musicme.backend.data.database.accounts

import com.vguivarc.musicme.backend.domain.models.account.Account
import com.vguivarc.musicme.backend.domain.models.nested.AccountStatus
import com.vguivarc.musicme.backend.domain.models.nested.Authority
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
    var email: String? = null,

    @Field("device_id")
    @Updatable
    var deviceId: String? = null,

    @Updatable
    var password: String? = null,

    @Updatable
    var authority: Authority = Authority.USER,

    @Updatable
    var status: AccountStatus = AccountStatus.NEW,

    var secret: String? = null,

    var uuid: String = UUID.randomUUID().toString(),

    @CreatedBy
    @Field("created_by")
    var createdBy: String? = null,

    @Field("created_date")
    @CreatedDate
    var createdDate: ZonedDateTime = ZonedDateTime.now(),

    @LastModifiedBy
    @Field("last_modified_by")
    var lastModifiedBy: String? = null,

    @LastModifiedDate
    @Field("last_modified_date")
    var lastModifiedDate: ZonedDateTime = ZonedDateTime.now(),

    @Version
    var version: Long? = null
) : IAccountResponse {

    override fun toAccount(): Account {
        return Account(
            id = id ?: "",
            password = password,
            uuid = uuid,
            email = email,
            deviceId = deviceId,
            authority = authority,
            secret = secret,
            status = status,
            createdDate = createdDate
        )
    }
}
