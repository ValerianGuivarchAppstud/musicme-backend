package com.vguivarc.musicme.backend.data.database.profile

import com.vguivarc.musicme.backend.domain.models.profile.Profile
import com.vguivarc.musicme.backend.domain.providers.profile.responses.IProfileResponse
import com.vguivarc.musicme.backend.libraries.entities.Updatable
import org.springframework.data.annotation.Id

class DBProfile(

    @Id
    var id: String? = null,

    var idAccount: String? = null,

    @Updatable
    var firstName: String? = null,

    @Updatable
    var lastName: String? = null

) : IProfileResponse {

    override fun toProfile(): Profile {
        return Profile(
            id = id ?: "",
            idAccount = idAccount ?: "",
            firstName = firstName,
            lastName = lastName
        )
    }
}
