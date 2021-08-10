package com.vguivarc.musicme.backend.web.api.v1.client.auth.requests

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@ApiModel(description = "the request to login with a device id")
data class LoginWithDeviceRequest(
    @field:NotNull
    @field:NotBlank
    @field:Size(min = 12)
    @ApiModelProperty(value = "the device id", required = true)
    val deviceId: String
)
