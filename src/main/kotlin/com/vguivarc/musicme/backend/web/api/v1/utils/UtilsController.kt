package com.vguivarc.musicme.backend.web.api.v1.utils

import com.vguivarc.musicme.backend.web.api.v1.utils.entities.BuildInfoDAO
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/v1/utils")
@RestController
@Api(
    value = "utils controller"
)
class UtilsController {

    @GetMapping("/build_infos")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "get the build informations")
    fun getBuildInfos(): BuildInfoDAO {
        return BuildInfoDAO.fromBuildProperties()
    }
}
