package com.vguivarc.musicme.backend.domain.services

import com.vguivarc.musicme.backend.domain.models.profile.Profile
import com.vguivarc.musicme.backend.domain.providers.profile.IProfileProvider
import com.vguivarc.musicme.backend.libraries.entities.EntityUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ProfileService {

    @Autowired
    lateinit var profileProvider: IProfileProvider

    fun createProfile(profile: Profile): Profile {
        return profileProvider.create(profile).toProfile()
    }

    fun updateProfile(profile: Profile, profileUpdate: Profile): Profile {
        return profileProvider.update(
            EntityUtils.update(profile, profileUpdate).copy(firstName = profile.firstName, lastName = profile.lastName)
        ).toProfile()
    }

    fun getProfile(idAccount: String): Profile? {
        return profileProvider.findByIdAccount(idAccount)?.toProfile()
    }

    fun findProfileWithIdAccount(idAccount: String): Profile {
        return profileProvider.findOneByIdAccount(idAccount).toProfile()
    }
}
