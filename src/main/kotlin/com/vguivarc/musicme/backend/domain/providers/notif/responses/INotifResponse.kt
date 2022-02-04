package com.vguivarc.musicme.backend.domain.providers.notif.responses

import com.vguivarc.musicme.backend.domain.models.notif.Notif

interface INotifResponse {
    fun toNotif(): Notif
}
