package com.vguivarc.musicme.backend.libraries.errors

import org.springframework.http.HttpStatus
import java.io.Serializable

interface AbstractError : Serializable {
    val statusCode: HttpStatus
    val type: String
    val message: String?
    val details: Collection<Serializable>
    val fields: Collection<Serializable>
}
