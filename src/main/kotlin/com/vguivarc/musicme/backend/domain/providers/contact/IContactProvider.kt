package com.vguivarc.musicme.backend.domain.providers.contact

import com.vguivarc.musicme.backend.domain.models.profile.Profile
import com.vguivarc.musicme.backend.domain.models.song.Song
import com.vguivarc.musicme.backend.domain.providers.contact.responses.IContactResponse
import com.vguivarc.musicme.backend.domain.providers.favorite.responses.IFavoriteResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface IContactProvider {
    fun getContactList(profile: Profile): List<IContactResponse>

    fun saveFavoriteStatus(profileId: String, profileContactId: String, isContact: Boolean)
}