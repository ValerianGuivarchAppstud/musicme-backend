package com.vguivarc.musicme.backend.web.api.v1.admin.account.entities

import com.fasterxml.jackson.annotation.JsonProperty
import com.vguivarc.musicme.backend.domain.models.account.Account
import com.vguivarc.musicme.backend.domain.models.nested.AccountStatus
import com.vguivarc.musicme.backend.domain.models.nested.Authority
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.ZonedDateTime

@ApiModel(value = "BoAccount", description = "the account details")
data class BoAccount(
    @ApiModelProperty(value = "the id", required = true, allowEmptyValue = false)
    var id: String,
    @ApiModelProperty(value = "the email", required = true, allowEmptyValue = false)
    var email: String,
    @ApiModelProperty(value = "the authority", required = true, allowEmptyValue = false)
    var authority: Authority,
    @ApiModelProperty(value = "the status", required = true, allowEmptyValue = false)
    var status: AccountStatus,
    @ApiModelProperty(value = "the creation date", required = true, allowEmptyValue = false)
    @JsonProperty("created_date")
    var createdDate: ZonedDateTime?

) {
    companion object {
        fun fromAccount(account: Account) = BoAccount(
            id = account.id,
            email = account.email ?: "",
            status = account.status,
            authority = account.authority,
            createdDate = account.createdDate
        )
    }
}
