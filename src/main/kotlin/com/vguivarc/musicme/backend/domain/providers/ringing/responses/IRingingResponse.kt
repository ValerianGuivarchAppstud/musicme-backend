package com.vguivarc.musicme.backend.domain.providers.ringing.responses

import com.vguivarc.musicme.backend.domain.models.ringing.Ringing

interface IRingingResponse {
    fun toRinging(): Ringing
}
