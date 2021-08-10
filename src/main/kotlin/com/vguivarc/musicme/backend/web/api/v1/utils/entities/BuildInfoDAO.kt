package com.vguivarc.musicme.backend.web.api.v1.utils.entities

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel(value = "BuildInfo", description = "build informations")
data class BuildInfoDAO(

    @ApiModelProperty(value = "the application name", required = true)
    val name: String? = "swag",
    @ApiModelProperty(value = "the build version")
    val version: String? = "v1.0.0",
    @ApiModelProperty(value = "the build time")
    val buildTime: String? = null

) {
    companion object {
        fun fromBuildProperties() = BuildInfoDAO()
    }
}
