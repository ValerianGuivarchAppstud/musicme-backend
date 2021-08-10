package com.vguivarc.musicme.backend.web.api.v1.client.auth.requests

import com.vguivarc.musicme.backend.domain.models.account.Account
import com.vguivarc.musicme.backend.domain.models.nested.AccountStatus
import com.vguivarc.musicme.backend.domain.models.nested.Authority
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@ApiModel(description = "the request to register an account")
data class RegisterWithPasswordRequest(
    @field:Email
    @field:NotNull
    @field:NotBlank
    @ApiModelProperty(value = "the password", required = true)
    val email: String,
    @field:NotNull
    @field:NotBlank
    @ApiModelProperty(value = "the email", required = true)
    val password: String
) {
    fun toAccount() = Account(
        email = this.email,
        password = this.password,
        authority = Authority.USER,
        status = AccountStatus.NEW
    )
}
