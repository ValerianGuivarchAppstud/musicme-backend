package com.vguivarc.musicme.backend.domain.services

import com.vguivarc.musicme.backend.domain.models.favorite.Favorite
import com.vguivarc.musicme.backend.domain.models.profile.Profile
import com.vguivarc.musicme.backend.domain.models.song.Song
import com.vguivarc.musicme.backend.domain.providers.favorite.IFavoriteProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FavoriteService {

    @Autowired
    lateinit var favoriteProvider: IFavoriteProvider

    fun saveFavoriteStatus(profile: Profile, song: Song, isFavorite: Boolean) {
        this.favoriteProvider.saveFavoriteStatus(profile, song, isFavorite)
    }

    fun getFavorites(profile: Profile): List<Favorite> {
        return this.favoriteProvider.getFavoriteList(profile).map{ it.toFavorite() }
    }
}
