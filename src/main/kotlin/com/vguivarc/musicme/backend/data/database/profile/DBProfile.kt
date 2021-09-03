package com.vguivarc.musicme.backend.data.database.profile

import com.vguivarc.musicme.backend.domain.models.profile.Profile
import com.vguivarc.musicme.backend.domain.providers.profile.responses.IProfileResponse
import com.vguivarc.musicme.backend.libraries.entities.Updatable
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document(collection = "profiles")
data class DBProfile(

    @Id
    @Field("id")
    var id: String? = null,

    @Field("idAccount")
    var idAccount: String? = null,

    @Updatable
    @Field("username")
    var username: String? = null

) : IProfileResponse {

    override fun toProfile(): Profile {
        return Profile(
            id = id ?: "",
            idAccount = idAccount ?: "",
            username = username
        )
    }
}
