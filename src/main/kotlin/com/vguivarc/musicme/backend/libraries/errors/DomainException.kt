package com.vguivarc.musicme.backend.libraries.errors

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.validation.FieldError

class DomainException : RuntimeException, AbstractError {

    override var type = "UNDEFINED_ERROR"
    override var statusCode = HttpStatus.INTERNAL_SERVER_ERROR
    override var details: MutableCollection<String> = ArrayList()
    override var fields: ArrayList<FieldErrorVM> = ArrayList()
    var headers: MutableList<HttpHeaders>? = null

    constructor(exception: AbstractError) : super(exception.message) {
        this.type = exception.type
        this.statusCode = exception.statusCode
    }

    constructor(message: String, exception: AbstractError) : super(message) {
        this.type = exception.type
        this.statusCode = exception.statusCode
    }

    constructor(details: Collection<String>, exception: AbstractError) : super(exception.message) {
        this.details = details.toMutableList()
        this.type = exception.type
        this.statusCode = exception.statusCode
    }

    constructor(type: String, message: String) : this(message, BaseExceptions.INTERNAL_SERVER_ERROR) {
        this.type = type
    }

    fun add(message: String): DomainException {
        details.add(message)
        return this
    }

    fun field(fieldError: FieldError): DomainException {
        return field(FieldErrorVM(fieldError.objectName, fieldError.field, fieldError.defaultMessage ?: ""))
    }

    private fun field(fieldError: FieldErrorVM): DomainException {
        fields.add(fieldError)
        return this
    }

    fun header(header: HttpHeaders): DomainException {
        if (headers == null)
            headers = ArrayList()
        headers!!.add(header)
        return this
    }

    val fieldErrors: List<FieldErrorVM>?
        get() = this.fields
}
