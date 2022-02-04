package com.vguivarc.musicme.backend.domain.models.notif

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.ZonedDateTime
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@ApiModel(description = "the notif description")
data class Notif(
    @field:NotNull
    @field:NotBlank
    @ApiModelProperty(value = "the notif id", allowEmptyValue = true)
    val id: String = "",
    val createdDate: ZonedDateTime = ZonedDateTime.now(),
    @field:NotNull
    @field:NotBlank
    @ApiModelProperty(value = "the type of the notif", allowEmptyValue = true)
    val type: NotifType = NotifType.RINGING_RECEIVED,
    val senderId: String = "",
    @ApiModelProperty(value = "the sender", allowEmptyValue = true)
    val receiverId: String = ""
)