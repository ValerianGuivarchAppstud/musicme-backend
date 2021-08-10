package com.vguivarc.musicme.backend.domain.providers.account.responses

import com.vguivarc.musicme.backend.domain.models.account.Account

interface IAccountResponse {
    fun toAccount(): Account
}
