package com.vguivarc.musicme.backend.web.api.v1.user.auth.requests

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel(description = "the jwt refresh token request")
data class JwtRefreshAuthRequest(
    @JsonProperty("accessToken")
    @ApiModelProperty(value = "the access token", required = true)
    val accessToken: String,
    @JsonProperty("refreshToken")
    @ApiModelProperty(value = "the refresh token", required = true)
    val refreshToken: String
)
