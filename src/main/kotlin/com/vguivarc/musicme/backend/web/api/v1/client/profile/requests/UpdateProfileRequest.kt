package com.vguivarc.musicme.backend.web.api.v1.client.profile.requests

import com.vguivarc.musicme.backend.domain.models.account.Account
import com.vguivarc.musicme.backend.domain.models.profile.Profile
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@ApiModel(description = "the request to update the client profile")
data class UpdateProfileRequest(
    @field:Email
    @field:NotNull
    @field:NotBlank
    @ApiModelProperty(value = "the new email", allowEmptyValue = true)
    val email: String?,
    @field:NotNull
    @field:NotBlank
    @ApiModelProperty(value = "the new first name", allowEmptyValue = true)
    val firstName: String?,
    @field:NotNull
    @field:NotBlank
    @ApiModelProperty(value = "the new last name", allowEmptyValue = true)
    val lastName: String?
) {
    fun toAccount() = Account(
        email = email
    )

    fun toProfile() = Profile(
        firstName = firstName,
        lastName = lastName
    )
}
