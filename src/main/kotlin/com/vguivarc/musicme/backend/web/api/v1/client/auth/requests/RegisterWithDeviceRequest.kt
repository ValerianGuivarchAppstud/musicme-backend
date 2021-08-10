package com.vguivarc.musicme.backend.web.api.v1.client.auth.requests

import com.vguivarc.musicme.backend.domain.models.account.Account
import com.vguivarc.musicme.backend.domain.models.nested.AccountStatus
import com.vguivarc.musicme.backend.domain.models.nested.Authority
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@ApiModel(description = "the request to register a new device")
data class RegisterWithDeviceRequest(
    @field:NotNull
    @field:NotBlank
    @ApiModelProperty(value = "the account id", required = true)
    val deviceId: String
) {
    fun toAccount() = Account(
        deviceId = this.deviceId,
        authority = Authority.USER,
        status = AccountStatus.NEW
    )
}
