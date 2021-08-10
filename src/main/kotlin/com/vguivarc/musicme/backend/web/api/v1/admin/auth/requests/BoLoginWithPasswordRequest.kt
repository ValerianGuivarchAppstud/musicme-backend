package com.vguivarc.musicme.backend.web.api.v1.admin.auth.requests

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@ApiModel(description = "the request to login with password")
data class BoLoginWithPasswordRequest(
    @field:Email
    @field:NotNull
    @field:NotBlank
    @ApiModelProperty(value = "the email", required = true, allowEmptyValue = false)
    val email: String,
    @field:NotNull
    @field:NotBlank
    @ApiModelProperty(value = "the password", required = true, allowEmptyValue = false)
    val password: String
)
