package com.vguivarc.musicme.backend.domain.providers.favorite.responses

import com.vguivarc.musicme.backend.domain.models.favorite.Favorite

interface IFavoriteResponse {
    fun toFavorite(): Favorite
}
