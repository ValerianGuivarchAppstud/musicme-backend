package com.vguivarc.musicme.backend.domain.models.account

import com.vguivarc.musicme.backend.domain.models.nested.AccountStatus
import com.vguivarc.musicme.backend.libraries.entities.Updatable
import java.time.ZonedDateTime

data class Account(
    val id: String = "",
    val createdDate: ZonedDateTime = ZonedDateTime.now(),
    @Updatable
    val phone: String? = null,
    @Updatable
    var email: String? = null,
    val deviceId: String? = null,
    var password: String? = null,
    val uuid: String? = null,
    @Updatable
    val status: AccountStatus = AccountStatus.NEW,
    val secret: String? = null
)
