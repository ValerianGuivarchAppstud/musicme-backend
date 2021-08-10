package com.vguivarc.musicme.backend.libraries.errors

import org.springframework.http.HttpStatus
import java.io.Serializable

enum class ProviderExceptions(
    private var givenMessage: String,
    private val givenHttpStatus: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR
) : AbstractError {
    LDF_ACCOUNT_NOT_FOUND
    ("Client not found in database", HttpStatus.NOT_FOUND),
    PROVIDER_NOT_FOUND
    ("Provider not found for this type of entity", HttpStatus.NOT_FOUND),
    DB_ACCOUNT_NOT_FOUND
    ("Client not found in our database", HttpStatus.NOT_FOUND),
    DB_ACCOUNT_ALREADY_EXISTS
    ("Client already exists", HttpStatus.BAD_REQUEST),
    DB_PROFILE_NOT_FOUND
    ("Profile not found in our database", HttpStatus.NOT_FOUND),
    DB_PROFILE_ALREADY_EXISTS
    ("Profile already exists", HttpStatus.BAD_REQUEST),
    DB_INQUIRY_NOT_FOUND
    ("Inquiry not found in our database", HttpStatus.NOT_FOUND),
    MISSING_FIELD
    ("Missing field in order to persist in our database", HttpStatus.NOT_FOUND),
    DB_CODE_NOT_FOUND
    ("We could not found verification code in our database", HttpStatus.NOT_FOUND),
    SERVICE_DISABLED
    ("Service disabled, check application properties", HttpStatus.FAILED_DEPENDENCY),
    CODE_ALREADY_SENT
    ("A code has already been sent, please wait until this code expires.", HttpStatus.NOT_ACCEPTABLE),
    MISSING_PROPERTY
    ("Missing property, check application properties", HttpStatus.FAILED_DEPENDENCY);

    override val statusCode: HttpStatus
        get() = this.givenHttpStatus
    override val type: String
        get() = this.name
    override val message: String
        get() = this.givenMessage
    override val details: List<Serializable>
        get() = ArrayList()
    override val fields: List<Serializable>
        get() = ArrayList()
}
