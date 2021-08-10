package com.vguivarc.musicme.backend.libraries.errors

import org.springframework.http.HttpStatus
import java.io.Serializable

enum class BaseExceptions(
    private var givenMessage: String,
    private val givenHttpStatus: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR
) : AbstractError {
    INTERNAL_SERVER_ERROR
    ("Internal errors. You may retry the request.", HttpStatus.INTERNAL_SERVER_ERROR),
    FATAL_SERVER_ERROR
    ("Something went wrong on our side. This request may always fail.", HttpStatus.INTERNAL_SERVER_ERROR),
    METHOD_NOT_ALLOWED
    ("This method or content-type is not supported by the API.", HttpStatus.METHOD_NOT_ALLOWED),
    CONFLICT_ERROR
    ("A conflict was detected. You may retry this request.", HttpStatus.CONFLICT),
    VALIDATION_FAILURE
    (
        "Missing or invalid parameter. This is most likely a developer mistake, please check request body.",
        HttpStatus.BAD_REQUEST
    ),
    FILE_TOO_BIG
    ("This file is too big. Please select a smaller file.", HttpStatus.BAD_REQUEST),
    FILE_ALREADY_BEEN_SENT
    ("This document validation has already been treated", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED
    ("This resource requires auth.", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED
    ("Access denied.", HttpStatus.FORBIDDEN),
    RESOURCE_NOT_FOUND
    ("Resource(s) not found.", HttpStatus.NOT_FOUND),
    MEDIA_TYPE_NOT_SUPPORTED
    ("Media type is not supported by this endpoint.", HttpStatus.METHOD_NOT_ALLOWED);

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
