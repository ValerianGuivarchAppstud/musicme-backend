package com.vguivarc.musicme.backend.web.api.v1.contact.request

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@ApiModel(description = "the request to update the user profile")
data class UpdateContactStatusRequest(
    @field:NotNull
    @field:NotBlank
    @ApiModelProperty(value = "the id contact", allowEmptyValue = true)
    val profileContactId: String,
    @field:NotNull
    @ApiModelProperty(value = "the contact status", allowEmptyValue = true)
    val status: Boolean
)
