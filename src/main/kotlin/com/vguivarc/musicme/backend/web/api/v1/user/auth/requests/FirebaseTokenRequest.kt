package com.vguivarc.musicme.backend.web.api.v1.user.auth.requests

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel(description = "the adding firebase token request")
data class FirebaseTokenRequest(
    @JsonProperty("token")
    @ApiModelProperty(value = "the access token", required = true)
    val token: String
)

