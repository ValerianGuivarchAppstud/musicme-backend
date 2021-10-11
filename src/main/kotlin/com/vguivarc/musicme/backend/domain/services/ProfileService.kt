package com.vguivarc.musicme.backend.domain.services

import com.vguivarc.musicme.backend.domain.models.account.Account
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
            EntityUtils.update(profile, profileUpdate)
        ).toProfile()
    }

    fun findProfileWithIdProfile(idProfile: String): Profile {
        return profileProvider.findOneByIdProfile(idProfile).toProfile()
    }

    fun findProfileWithIdAccount(idAccount: String): Profile {
        return profileProvider.findOneByIdAccount(idAccount).toProfile()
    }

    fun findListByIdAccount(accountList: List<Account>): List<Profile> {
        return profileProvider.findListByIdAccount(accountList.map { it.idAccount }).map { it.toProfile() }
    }
}
