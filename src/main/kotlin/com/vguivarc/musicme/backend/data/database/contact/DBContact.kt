package com.vguivarc.musicme.backend.data.database.contact

import com.vguivarc.musicme.backend.domain.models.contact.Contact
import com.vguivarc.musicme.backend.domain.providers.contact.responses.IContactResponse
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.ZonedDateTime

@Document(collection = "contacts")
data class DBContact(

    @Id
    var id: String? = null,

    @Field("id_profile")
    var idProfile: String = "",

    @Field("id_profile_of_contact")
    var idProfileOfContact: String = "",

    @Field("created_date")
    @CreatedDate
    var createdDate: ZonedDateTime = ZonedDateTime.now()

) : IContactResponse {

    override fun toContact(): Contact {
        return Contact(
            idProfile = idProfile,
            idProfileOfContact = idProfileOfContact,
            createdDate = createdDate
        )
    }
}
