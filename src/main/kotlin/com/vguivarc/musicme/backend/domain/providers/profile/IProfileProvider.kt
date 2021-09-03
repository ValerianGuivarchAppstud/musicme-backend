package com.vguivarc.musicme.backend.domain.providers.profile

import com.vguivarc.musicme.backend.domain.models.profile.Profile
import com.vguivarc.musicme.backend.domain.providers.profile.responses.IProfileResponse

interface IProfileProvider {
    fun create(toCreate: Profile): IProfileResponse
    fun update(profile: Profile): IProfileResponse

    fun findOneByIdAccount(id: String): IProfileResponse
    fun findListByIdAccount(idList: List<String>): List<IProfileResponse>
    fun findByIdAccount(id: String): IProfileResponse?
}
