package com.vguivarc.musicme.backend.web.api.v1.user.auth.entities

import com.fasterxml.jackson.annotation.JsonProperty
import com.vguivarc.musicme.backend.domain.models.auth.JwtAuthResponse
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel(value = "JWT access token", description = "the jwt access token")
data class JwtAuthResponseVM(
    @JsonProperty("access_token")
    @ApiModelProperty(value = "the access token")
    val accessToken: String,
    @JsonProperty("access_token_expiration")
    @ApiModelProperty(value = "the access token expiration date")
    val accessTokenExpiration: Long,
    @JsonProperty("refresh_token")
    @ApiModelProperty(value = "the refresh token")
    val refreshToken: String? = null,
    @JsonProperty("refresh_token_expiration")
    @ApiModelProperty(value = "the refresh token expiration date")
    val refreshTokenExpiration: Long? = null
) {
    companion object {
        fun fromJwtAuthResponse(jwtAuthResponse: JwtAuthResponse) = JwtAuthResponseVM(
            accessToken = jwtAuthResponse.accessToken,
            accessTokenExpiration = jwtAuthResponse.accessTokenExpiration,
            refreshToken = jwtAuthResponse.refreshToken,
            refreshTokenExpiration = jwtAuthResponse.refreshTokenExpiration
        )
    }
}
