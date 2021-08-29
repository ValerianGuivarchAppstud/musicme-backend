package com.vguivarc.musicme.backend.web.api.v1.user.auth.requests

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@ApiModel(description = "the request to login with password")
data class LoginWithFacebookRequest(
    @field:NotNull
    @field:NotBlank
    @ApiModelProperty(value = "the facebook token", required = true)
    val facebookToken: String,
    @field:NotNull
    @field:NotBlank
    @ApiModelProperty(value = "the password", required = true)
    val password: String
)
