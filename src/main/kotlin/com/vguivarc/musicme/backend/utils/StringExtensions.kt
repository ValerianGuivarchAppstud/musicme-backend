package com.vguivarc.musicme.backend.utils

fun String.sanitizeEmail(): String {
    return this.trim().toLowerCase()
}
