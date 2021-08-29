package com.vguivarc.musicme.backend.data.database.favorite

import com.vguivarc.musicme.backend.domain.models.profile.Profile
import com.vguivarc.musicme.backend.domain.models.song.Song
import com.vguivarc.musicme.backend.domain.providers.favorite.IFavoriteProvider
import com.vguivarc.musicme.backend.domain.providers.favorite.responses.IFavoriteResponse
import com.vguivarc.musicme.backend.libraries.errors.DomainException
import com.vguivarc.musicme.backend.libraries.errors.ProviderExceptions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class DBFavoriteProvider : IFavoriteProvider {

    @Autowired
    lateinit var repository: DBFavoriteRepository

    override fun getFavoriteList(profile: Profile): List<IFavoriteResponse> {
        return repository.findAllByIdOwnerContains(
            profile.id
        )
    }

    override fun saveFavoriteStatus(profile: Profile, song: Song, isFavorite: Boolean) {
        if(isFavorite) {
            repository.findOneByIdSong(song.id)
            ?.let {
                throw DomainException(ProviderExceptions.DB_FAVORITE_ALREADY_EXISTS)
            } ?:let{
                repository.save(DBFavorite(
                    idOwner = profile.id,
                    idSong = song.id,
                    title = song.title,
                    artworkUrl = song.artworkUrl
                ))
            }
        } else {
            repository.findOneByIdSong(song.id)
                ?.let {
                    repository.delete(it)
                } ?:let{
                throw DomainException(ProviderExceptions.DB_FAVORITE_NOT_FOUND)
            }
        }
    }
}