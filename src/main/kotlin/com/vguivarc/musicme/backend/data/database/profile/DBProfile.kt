package com.vguivarc.musicme.backend.data.database.profile

import com.vguivarc.musicme.backend.domain.models.profile.Profile
import com.vguivarc.musicme.backend.domain.providers.profile.responses.IProfileResponse
import com.vguivarc.musicme.backend.libraries.entities.Updatable
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.ZonedDateTime

@Document(collection = "profiles")
data class DBProfile(

    @Id
    var id: String? = null,

    @Field("idAccount")
    var idAccount: String? = null,

    @Updatable
    @Field("username")
    var username: String? = null,

    @Updatable
    @Field("dateLastSeen")
    var dateLastSeen: ZonedDateTime? = ZonedDateTime.now()

) : IProfileResponse {

    override fun toProfile(): Profile {
        return Profile(
            idProfile = id ?: "",
            idAccount = idAccount ?: "",
            username = username,
            dateLastSeen = dateLastSeen ?: ZonedDateTime.now()
        )
    }
}
