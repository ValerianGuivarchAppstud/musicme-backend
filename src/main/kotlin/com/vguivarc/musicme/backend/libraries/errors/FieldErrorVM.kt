package com.vguivarc.musicme.backend.libraries.errors

import org.springframework.validation.FieldError
import java.io.Serializable

class FieldErrorVM : Serializable {

    val objectName: String
    val field: String
    val message: String?
    val codes: Array<String>?
    val arguments: Array<Any>?

    constructor(dto: String, field: String, message: String) {
        this.codes = null
        this.arguments = null
        this.objectName = dto
        this.field = field
        this.message = message
    }

    constructor(dto: String, field: String, message: String, codes: Array<String>, arguments: Array<Any>) {
        this.objectName = dto
        this.field = field
        this.message = message
        this.codes = codes
        this.arguments = arguments
    }

    constructor(error: FieldError) {
        this.codes = error.codes
        this.arguments = error.arguments
        this.objectName = error.objectName
        this.field = error.field
        this.message = error.defaultMessage
    }
}
