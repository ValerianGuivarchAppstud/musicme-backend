package com.vguivarc.musicme.backend.domain.providers.profile.responses

import com.vguivarc.musicme.backend.domain.models.profile.Profile

interface IProfileResponse {
    fun toProfile(): Profile
}
