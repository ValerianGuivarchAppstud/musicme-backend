package com.vguivarc.musicme.backend.domain.models.nested

enum class Authority(val value: String) {
    ADMIN("Admin"),
    USER("User"),
    UNKNOWN("Unknown");

    companion object {

        fun fromString(type: String?): Authority {
            return values().firstOrNull {
                it.value == type
            } ?: UNKNOWN
        }
    }
}
