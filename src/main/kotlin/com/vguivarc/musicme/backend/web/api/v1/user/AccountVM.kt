package com.vguivarc.musicme.backend.web.api.v1.user

import com.fasterxml.jackson.annotation.JsonInclude
import com.vguivarc.musicme.backend.domain.models.account.Account
import com.vguivarc.musicme.backend.domain.models.nested.AccountStatus
import com.vguivarc.musicme.backend.domain.models.profile.Profile
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "Account", description = "all details about the account")
data class AccountVM(

    @ApiModelProperty(value = "the account id", required = true)
    val profileId: String,
    @ApiModelProperty(value = "the account email")
    val email: String? = null,
    @ApiModelProperty(value = "the account device id")
    val deviceId: String? = null,
    @ApiModelProperty(value = "the account status", required = true)
    val status: AccountStatus,
    @ApiModelProperty(value = "the account uuid", required = true)
    val uuid: String,
    @ApiModelProperty(value = "the account nick name")
    val nickname: String? = null

) {
    companion object {
        fun fromAccountAndProfile(account: Account, profile: Profile) = AccountVM(
            profileId = profile.id,
            email = account.email,
            deviceId = account.deviceId,
            uuid = account.uuid ?: "",
            status = account.status,
            nickname = profile.nickname
        )
    }
}
