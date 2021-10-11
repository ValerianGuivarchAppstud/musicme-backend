package com.vguivarc.musicme.backend.web.api.v1.ringing.entities

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel(value = "JWT access token", description = "the jwt access token")
data class NextRingingResponseVM(
    @JsonProperty("nextRinging")
    @ApiModelProperty(value = "the next ringing, or null")
    val nextRinging: RingingVM? = null
)