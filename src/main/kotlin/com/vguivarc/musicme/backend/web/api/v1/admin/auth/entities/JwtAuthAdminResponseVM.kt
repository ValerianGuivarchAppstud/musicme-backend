package com.vguivarc.musicme.backend.web.api.v1.admin.auth.entities

import com.fasterxml.jackson.annotation.JsonProperty
import com.vguivarc.musicme.backend.domain.models.auth.JwtAuthResponse
import com.vguivarc.musicme.backend.domain.models.nested.Authority
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel(value = "AdminJwtAccessToken", description = "the jwt access token")
data class JwtAuthAdminResponseVM(
    @JsonProperty("access_token")
    @ApiModelProperty(value = "the access token", required = true, allowEmptyValue = false)
    val accessToken: String,
    @JsonProperty("access_token_expiration")
    @ApiModelProperty(value = "the access token expiration date", required = true, allowEmptyValue = false)
    val accessTokenExpiration: Long,
    @JsonProperty("refresh_token")
    @ApiModelProperty(value = "the refresh token", required = true, allowEmptyValue = false)
    val refreshToken: String? = null,
    @JsonProperty("refresh_token_expiration")
    @ApiModelProperty(value = "the refresh token expiration date", required = true, allowEmptyValue = false)
    val refreshTokenExpiration: Long? = null,
    @JsonProperty("authority")
    @ApiModelProperty(value = "the authority", required = true, allowEmptyValue = false)
    val authority: Authority
) {
    companion object {
        fun fromJwtAuthResponse(jwtAuthResponse: JwtAuthResponse) = JwtAuthAdminResponseVM(
            accessToken = jwtAuthResponse.accessToken,
            accessTokenExpiration = jwtAuthResponse.accessTokenExpiration,
            refreshToken = jwtAuthResponse.refreshToken,
            refreshTokenExpiration = jwtAuthResponse.refreshTokenExpiration,
            authority = jwtAuthResponse.authority
        )
    }
}
