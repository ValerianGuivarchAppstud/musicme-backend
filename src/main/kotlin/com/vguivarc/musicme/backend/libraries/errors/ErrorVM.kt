package com.vguivarc.musicme.backend.libraries.errors

import com.fasterxml.jackson.annotation.JsonInclude
import java.io.Serializable
import java.util.Date

@JsonInclude(JsonInclude.Include.NON_NULL)
class ErrorVM(ex: AbstractError) : Serializable {

    var timestamp: Long = 0
    var status: Int = 0
    var error: String
    var message: String?
    var path: String?
    var details: Collection<Serializable>
    var fields: Collection<Serializable>

    init {
        this.timestamp = Date().time
        this.error = ex.type
        this.message = ex.message
        this.status = ex.statusCode.value()
        this.details = ex.details
        this.fields = ex.fields
        this.path = null
    }

    constructor(path: String?, ex: AbstractError, message: String) : this(ex) {
        this.path = path
        this.message = message
    }
}
