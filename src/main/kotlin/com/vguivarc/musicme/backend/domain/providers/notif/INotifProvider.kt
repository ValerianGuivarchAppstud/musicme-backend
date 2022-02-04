package com.vguivarc.musicme.backend.domain.providers.notif

import com.vguivarc.musicme.backend.domain.models.notif.Notif
import com.vguivarc.musicme.backend.domain.models.notif.NotifType
import com.vguivarc.musicme.backend.domain.providers.notif.responses.INotifResponse

interface INotifProvider {
    fun getNotifList(id: String): List<INotifResponse>
    fun sendNotif(idProfile: String, idProfileOfContact: String, type: NotifType): Notif
 }