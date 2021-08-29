package com.vguivarc.musicme.backend.data.database.favorite

import com.vguivarc.musicme.backend.domain.providers.favorite.responses.IFavoriteResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository

interface DBFavoriteRepository : MongoRepository<DBFavorite, String> {

    fun findAllByIdOwnerContains(idOwner: String): List<IFavoriteResponse>
    fun findOneByIdSong(idSong: String): DBFavorite?

}
