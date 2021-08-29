package com.vguivarc.musicme.backend.domain.models.auth

data class JwtAuthResponse(
    val accountId: String,
    val accessToken: String,
    val accessTokenExpiration: Long,
    val refreshToken: String? = null,
    val refreshTokenExpiration: Long? = null
)
