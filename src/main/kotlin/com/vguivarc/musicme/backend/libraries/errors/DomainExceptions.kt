package com.vguivarc.musicme.backend.libraries.errors

import org.springframework.http.HttpStatus
import java.io.Serializable

enum class DomainExceptions(
    private var givenMessage: String,
    private val givenHttpStatus: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR
) : AbstractError {
    CODE_NOT_FOUND(
        "We could not find this verification code",
        HttpStatus.NOT_FOUND
    ),
    ACCOUNT_NOT_FOUND(
        "We could not find this account",
        HttpStatus.NOT_FOUND
    ),
    ACCOUNT_ALREADY_EXISTS(
        "This account already exist",
        HttpStatus.CONFLICT
    ),
    ACCOUNT_EMAIL_ALREADY_REGISTERED(
        "This account already has an email registered",
        HttpStatus.CONFLICT
    ),
    ACCOUNT_NOT_CREATED(
        "We could not create this user",
        HttpStatus.INTERNAL_SERVER_ERROR
    ),
    INVALID_DATE(
        "This date is invalid (in the past or too close from now)",
        HttpStatus.BAD_REQUEST
    ),
    INVALID_EMAIL(
        "This email is invalid",
        HttpStatus.BAD_REQUEST
    );
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
