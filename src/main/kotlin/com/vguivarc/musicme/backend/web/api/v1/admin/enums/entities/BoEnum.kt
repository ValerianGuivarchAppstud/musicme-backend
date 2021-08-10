package com.vguivarc.musicme.backend.web.api.v1.admin.enums.entities

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel(value = "BoEnum", description = "the idea stats")
data class BoEnum(
    @ApiModelProperty(value = "the id", required = true, allowEmptyValue = false)
    val id: String,
    @ApiModelProperty(value = "the entity id", required = true, allowEmptyValue = false)
    val name: String
) {
    companion object {
        fun fromEnumValue(value: String) = BoEnum(
            id = value,
            name = value
        )
    }
}
