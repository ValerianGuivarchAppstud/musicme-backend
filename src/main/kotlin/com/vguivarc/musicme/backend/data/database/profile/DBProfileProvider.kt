package com.vguivarc.musicme.backend.data.database.profile

import com.vguivarc.musicme.backend.domain.models.profile.Profile
import com.vguivarc.musicme.backend.domain.providers.profile.IProfileProvider
import com.vguivarc.musicme.backend.domain.providers.profile.responses.IProfileResponse
import com.vguivarc.musicme.backend.libraries.entities.EntityUtils
import com.vguivarc.musicme.backend.libraries.errors.DomainException
import com.vguivarc.musicme.backend.libraries.errors.ProviderExceptions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DBProfileProvider : IProfileProvider {

    @Autowired
    lateinit var repository: DBProfileRepository

    fun Profile.toDBProfile(): DBProfile {
        return DBProfile(
            id = if (this.id.isBlank()) { null } else { this.id },
            idAccount = idAccount,
            username = username
        )
    }

    override fun create(toCreate: Profile): IProfileResponse {
        val profileToCreate = toCreate.toDBProfile()

        // check if account already exists
        toCreate.idAccount.let {
            repository.findOneByIdAccount(it)?.let {
                throw DomainException(ProviderExceptions.DB_PROFILE_ALREADY_EXISTS)
            }
        }

        return repository.save(profileToCreate)
    }

    override fun update(profile: Profile): IProfileResponse {
        val baseProfile = repository.findOneById(
            profile.id
        ) ?: throw DomainException(ProviderExceptions.DB_PROFILE_NOT_FOUND)

        return repository.save(EntityUtils.update(baseProfile, profile.toDBProfile()))
    }

    override fun findOneByIdAccount(id: String): IProfileResponse {
        return repository.findOneByIdAccount(
            id
        ) ?: throw DomainException(ProviderExceptions.DB_PROFILE_NOT_FOUND)
    }

    override fun findListByIdAccount(idList: List<String>): List<IProfileResponse> {
        return idList.mapNotNull { repository.findOneByIdAccount(it) }
    }

    override fun findByIdAccount(id: String): IProfileResponse? {
        return repository.findOneByIdAccount(id)
    }
}
