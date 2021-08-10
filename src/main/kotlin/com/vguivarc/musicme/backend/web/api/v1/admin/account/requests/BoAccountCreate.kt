package com.vguivarc.musicme.backend.web.api.v1.admin.account.requests

import com.fasterxml.jackson.annotation.JsonInclude
import com.vguivarc.musicme.backend.domain.models.account.Account
import com.vguivarc.musicme.backend.domain.models.nested.AccountStatus
import com.vguivarc.musicme.backend.domain.models.nested.Authority
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "BoAccountCreate", description = "all details about the account")
data class BoAccountCreate(
    @field:NotNull
    @field:NotBlank
    @field:Email
    @ApiModelProperty(value = "the email", required = true, allowEmptyValue = false)
    var email: String,
    @field:NotNull
    @field:NotBlank
    @ApiModelProperty(value = "the password", required = true, allowEmptyValue = false)
    var password: String,
    @field:NotNull
    @ApiModelProperty(value = "the authority", required = true, allowEmptyValue = false)
    var authority: Authority,
    @field:NotNull
    @ApiModelProperty(value = "the account status", required = true, allowEmptyValue = false)
    var status: AccountStatus
) {
    fun toAccount() = Account(
        email = email,
        password = password,
        authority = authority,
        status = status
    )
}
