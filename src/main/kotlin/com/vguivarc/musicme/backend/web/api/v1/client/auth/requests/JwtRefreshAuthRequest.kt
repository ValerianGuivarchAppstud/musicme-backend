package com.vguivarc.musicme.backend.web.api.v1.client.auth.requests

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel(description = "the jwt refresh token request")
data class JwtRefreshAuthRequest(
    @JsonProperty("access_token")
    @ApiModelProperty(value = "the access token", required = true)
    val accessToken: String,
    @JsonProperty("refresh_token")
    @ApiModelProperty(value = "the refresh token", required = true)
    val refreshToken: String
)
