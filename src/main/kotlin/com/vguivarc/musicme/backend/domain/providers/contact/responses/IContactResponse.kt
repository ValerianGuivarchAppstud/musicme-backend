package com.vguivarc.musicme.backend.domain.providers.contact.responses

import com.vguivarc.musicme.backend.domain.models.contact.Contact

interface IContactResponse {
    fun toContact(): Contact
}
