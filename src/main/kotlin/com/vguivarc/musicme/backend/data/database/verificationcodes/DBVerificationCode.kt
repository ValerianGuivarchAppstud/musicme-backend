package com.vguivarc.musicme.backend.data.database.verificationcodes

import com.vguivarc.musicme.backend.domain.models.VerificationCode
import com.vguivarc.musicme.backend.domain.providers.verificationcode.responses.IVerificationCodeResponse
import org.springframework.data.annotation.*
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.ZonedDateTime

@Document(collection = "verification_codes")
data class DBVerificationCode(

    @Id
    var id: String? = null,
    var code: String,
    @Field("expiration_date")
    var expirationDate: ZonedDateTime,
    @Field("account_id")
    var accountId: String,
    var isUsed: Boolean = false,
    var email: String? = null,

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

) : IVerificationCodeResponse {
    override fun toVerificationCode(): VerificationCode {
        return VerificationCode(
            id = id ?: "",
            code = code,
            expirationDate = expirationDate,
            accountId = accountId,
            isUsed = isUsed,
            email = email
        )
    }
}
