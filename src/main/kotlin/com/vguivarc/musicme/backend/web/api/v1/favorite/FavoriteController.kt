package com.vguivarc.musicme.backend.web.api.v1.favorite

import com.vguivarc.musicme.backend.domain.services.AccountService
import com.vguivarc.musicme.backend.domain.services.AuthenticationService
import com.vguivarc.musicme.backend.domain.services.FavoriteService
import com.vguivarc.musicme.backend.domain.services.ProfileService
import com.vguivarc.musicme.backend.web.api.v1.favorite.entities.FavoriteVM
import com.vguivarc.musicme.backend.web.api.v1.favorite.request.UpdateFavoriteStatusRequest
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RequestMapping("/api/v1/favorites")
@RestController
@Api(
    value = "favorite",
    description = "handle the favorite requests"
)
class FavoriteController {

    @Autowired
    lateinit var authenticationService: AuthenticationService

    @Autowired
    lateinit var accountService: AccountService

    @Autowired
    lateinit var profileService: ProfileService

    @Autowired
    lateinit var favoriteService: FavoriteService

    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "get the user favorites list")
    fun getFavoriteList(): List<FavoriteVM> {
        val account = authenticationService.findConnectedAccountOrThrowAccessDenied()

        val profile = profileService.findProfileWithIdAccount(account.idAccount)

        val list = favoriteService.getFavorites(profile)

        return list.map { FavoriteVM.fromFavorite(it) }
    }

    @PostMapping("status")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "add or remove a favorite song")
    fun updateFavoriteStatus(
        @Valid
        @RequestBody
        @ApiParam(value = "the request to add or remove a favorite song", required = true)
        updateFavoriteStatusRequest: UpdateFavoriteStatusRequest
    ) : List<FavoriteVM>{
        val account = authenticationService.findConnectedAccountOrThrowAccessDenied()

        val profile = profileService.findProfileWithIdAccount(account.idAccount)

        favoriteService.saveFavoriteStatus(profile, updateFavoriteStatusRequest.song, updateFavoriteStatusRequest.status)

        return getFavoriteList()
    }
}
