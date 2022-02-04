package com.vguivarc.musicme.backend.domain.models.notif

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

enum class NotifType {
    RINGING_USED,
    RINGING_RECEIVED
}