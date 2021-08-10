package com.vguivarc.musicme.backend.domain.models

import java.time.ZonedDateTime

data class VerificationCode(
    var id: String,
    var code: String,
    var expirationDate: ZonedDateTime,
    var accountId: String,
    var isUsed: Boolean = false,
    var email: String? = null
)
