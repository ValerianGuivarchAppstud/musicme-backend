package com.vguivarc.musicme.backend.web.api.v1.admin.account.requests

import com.vguivarc.musicme.backend.domain.models.account.Account
import com.vguivarc.musicme.backend.domain.models.nested.AccountStatus
import com.vguivarc.musicme.backend.domain.models.nested.Authority
import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "BoAccountUpdate", description = "all details about the account")
data class BoAccountUpdate(
    @field:NotNull
    @field:NotBlank
    @ApiModelProperty(value = "the id", required = true)
    val id: String,
    @field:NotNull
    @field:NotBlank
    @field:Email
    @ApiModelProperty(value = "the email")
    var email: String,
    @ApiModelProperty(value = "the password")
    var password: String? = null,
    @field:NotNull
    @ApiModelProperty(value = "the authority")
    var authority: Authority,
    @field:NotNull
    @ApiModelProperty(value = "the account status")
    var status: AccountStatus
) {
    fun toAccount() = Account(
        id = id,
        email = email,
        password = password,
        authority = authority,
        status = status
    )
}
