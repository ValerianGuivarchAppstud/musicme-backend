package com.vguivarc.musicme.backend.domain.services

import com.vguivarc.musicme.backend.domain.models.contact.Contact
import com.vguivarc.musicme.backend.domain.models.profile.Profile
import com.vguivarc.musicme.backend.domain.providers.contact.IContactProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service

@Service
class ContactService {

    @Autowired
    lateinit var contactProvider: IContactProvider

    fun saveContactStatus(profileId: String, profileContactId: String, isContact: Boolean) {
        this.contactProvider.saveFavoriteStatus(profileId, profileContactId, isContact)
    }

    fun getContacts(profile: Profile): List<Contact> {
        return this.contactProvider.getContactList(profile).map{ it.toContact() }
    }
}
