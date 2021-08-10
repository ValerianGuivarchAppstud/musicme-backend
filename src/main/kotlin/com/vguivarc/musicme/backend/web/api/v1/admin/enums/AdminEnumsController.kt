package com.vguivarc.musicme.backend.web.api.v1.admin.enums

import com.vguivarc.musicme.backend.domain.models.nested.AccountStatus
import com.vguivarc.musicme.backend.domain.models.nested.Authority
import com.vguivarc.musicme.backend.web.api.v1.admin.enums.entities.BoEnum
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@CrossOrigin
@RequestMapping("/api/v1/admin/enums")
@RestController
@Api(value = "admin-enums")
class AdminEnumsController {

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "admin > enums > get values for an enum")
    fun getAll(
        @RequestParam("enum")
        @ApiParam(required = false)
        enumName: String?
    ): Page<BoEnum> {
        return when (enumName) {
            "authority" -> PageImpl(
                Authority.values().filterNot { it == Authority.UNKNOWN }.map { BoEnum.fromEnumValue(it.name) }
            )
            "accountStatus" -> PageImpl(
                AccountStatus.values().map { BoEnum.fromEnumValue(it.name) }
            )
            else -> PageImpl(listOf())
        }
    }

    @GetMapping("/many")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "void controller to prevent backoffice errors", hidden = true)
    fun getMany(): Page<BoEnum> {
        return PageImpl(listOf())
    }
}
