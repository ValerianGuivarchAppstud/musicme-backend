package com.vguivarc.musicme.backend.domain.providers.contact

import com.vguivarc.musicme.backend.domain.models.profile.Profile
import com.vguivarc.musicme.backend.domain.providers.contact.responses.IContactResponse

interface IContactProvider {
    fun getContactList(profile: Profile): List<IContactResponse>

    fun saveFavoriteStatus(profileId: String, profileContactId: String, isContact: Boolean)
}