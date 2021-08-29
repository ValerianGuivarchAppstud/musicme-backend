package com.vguivarc.musicme.backend.data.database.ringing

import com.vguivarc.musicme.backend.domain.models.ringing.Ringing
import com.vguivarc.musicme.backend.domain.providers.ringing.responses.IRingingResponse
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "ringings")
data class DBRinging(

    @Id
    var id: String? = null

) : IRingingResponse {

    override fun toRinging(): Ringing {
        return Ringing(
            id = id ?: ""
        )
    }
}
