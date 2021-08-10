package com.vguivarc.musicme.backend.domain.models.auth

import com.vguivarc.musicme.backend.domain.models.nested.Authority

data class JwtAuthResponse(
    val authority: Authority,
    val accountId: String,
    val accessToken: String,
    val accessTokenExpiration: Long,
    val refreshToken: String? = null,
    val refreshTokenExpiration: Long? = null
)
