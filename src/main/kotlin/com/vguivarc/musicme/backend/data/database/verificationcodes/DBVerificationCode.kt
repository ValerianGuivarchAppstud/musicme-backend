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
    @Field("expirationDate")
    var expirationDate: ZonedDateTime,
    @Field("accountId")
    var accountId: String,
    @Field("isUsed")
    var isUsed: Boolean = false,
    @Field("email")
    var email: String? = null,

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
