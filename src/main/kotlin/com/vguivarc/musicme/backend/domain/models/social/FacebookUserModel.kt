package com.vguivarc.musicme.backend.domain.models.social

import com.fasterxml.jackson.annotation.JsonProperty
import com.vguivarc.musicme.backend.domain.models.account.Account
import com.vguivarc.musicme.backend.domain.models.profile.Profile
import lombok.Data

@Data
class FacebookUserModel {
    val id: String? = null
    @JsonProperty("firstName")
    val firstName: String? = null
    @JsonProperty("lastName")
    val lastName: String? = null
    val email: String? = null

    fun toAccount() : Account{
        return Account(
        email = this.email,
            facebookId = this.id
        )
    }

    fun toProfile(idAccount: String): Profile {
        return Profile(
            idAccount = idAccount,
            username = this.firstName+ " "+this.lastName
        )
    }
}