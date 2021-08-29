package com.vguivarc.musicme.backend.domain.models.contact

import java.time.ZonedDateTime

data class Contact (
    val idProfile: String = "",
    val idProfileOfContact: String = "",
    val createdDate: ZonedDateTime = ZonedDateTime.now()
)