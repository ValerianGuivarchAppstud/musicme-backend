package com.vguivarc.musicme.backend.libraries.errors

import org.springframework.http.HttpStatus
import java.io.Serializable

enum class ProviderExceptions(
    private var givenMessage: String,
    private val givenHttpStatus: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR
) : AbstractError {
    LDF_ACCOUNT_NOT_FOUND
    ("User not found in database", HttpStatus.NOT_FOUND),
    PROVIDER_NOT_FOUND
    ("Provider not found for this type of entity", HttpStatus.NOT_FOUND),
    DB_ACCOUNT_NOT_FOUND
    ("User not found in our database", HttpStatus.NOT_FOUND),
    DB_ACCOUNT_ALREADY_EXISTS
    ("User already exists", HttpStatus.BAD_REQUEST),
    DB_PROFILE_NOT_FOUND
    ("Profile not found in our database", HttpStatus.NOT_FOUND),
    DB_PROFILE_ALREADY_EXISTS
        ("Profile already exists", HttpStatus.BAD_REQUEST),
    DB_FAVORITE_ALREADY_EXISTS
        ("Favorite already exists", HttpStatus.BAD_REQUEST),
    DB_FAVORITE_NOT_FOUND
        ("Favorite not found in our database", HttpStatus.NOT_FOUND),
    DB_CONTACT_ALREADY_EXISTS
        ("Contact already exists", HttpStatus.BAD_REQUEST),
    DB_CONTACT_NOT_FOUND
        ("Contact not found in our database", HttpStatus.NOT_FOUND),
    DB_RINGING_NOT_FOUND
        ("Ringing not found in our database", HttpStatus.NOT_FOUND),
    ;

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
