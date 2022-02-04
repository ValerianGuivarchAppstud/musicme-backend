package com.vguivarc.musicme.backend.domain.providers.favorite

import com.vguivarc.musicme.backend.domain.models.profile.Profile
import com.vguivarc.musicme.backend.domain.models.song.Song
import com.vguivarc.musicme.backend.domain.providers.favorite.responses.IFavoriteResponse

interface IFavoriteProvider {
    fun getFavoriteList(profile: Profile): List<IFavoriteResponse>

    fun saveFavoriteStatus(profile: Profile, song: Song, isFavorite: Boolean)

}