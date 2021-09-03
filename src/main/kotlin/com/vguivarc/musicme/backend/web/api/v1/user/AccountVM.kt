package com.vguivarc.musicme.backend.web.api.v1.user

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.vguivarc.musicme.backend.domain.models.account.Account
import com.vguivarc.musicme.backend.domain.models.nested.AccountStatus
import com.vguivarc.musicme.backend.domain.models.profile.Profile
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "Account", description = "all details about the account")
data class AccountVM(
    @JsonProperty("profileId")
    @ApiModelProperty(value = "the account id", required = true)
    val profileId: String,
    @JsonProperty("email")
    @ApiModelProperty(value = "the account email")
    val email: String? = null,
    @JsonProperty("facebookId")
    @ApiModelProperty(value = "the account facebookId id")
    val facebookId: String? = null,
    @JsonProperty("status")
    @ApiModelProperty(value = "the account status", required = true)
    val status: AccountStatus,
    @JsonProperty("uuid")
    @ApiModelProperty(value = "the account uuid", required = true)
    val uuid: String,
    @JsonProperty("username")
    @ApiModelProperty(value = "the account nick name")
    val username: String? = null

) {
    companion object {
        fun fromAccountAndProfile(account: Account, profile: Profile) = AccountVM(
            profileId = profile.id,
            email = account.email,
            facebookId = account.facebookId,
            uuid = account.uuid ?: "",
            status = account.status,
            username = profile.username
        )
    }
}
