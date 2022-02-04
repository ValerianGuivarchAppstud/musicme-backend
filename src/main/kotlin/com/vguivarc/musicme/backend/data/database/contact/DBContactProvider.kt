package com.vguivarc.musicme.backend.data.database.contact

import com.vguivarc.musicme.backend.domain.models.profile.Profile
import com.vguivarc.musicme.backend.domain.providers.contact.IContactProvider
import com.vguivarc.musicme.backend.domain.providers.contact.responses.IContactResponse
import com.vguivarc.musicme.backend.libraries.errors.DomainException
import com.vguivarc.musicme.backend.libraries.errors.ProviderExceptions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class DBContactProvider : IContactProvider {

    @Autowired
    lateinit var repository: DBContactRepository
    override fun getContactList(profile: Profile): List<IContactResponse> {
        val list = repository.findAllByIdProfile(profile.idProfile)
        return list
    }

    override fun saveFavoriteStatus(profileId: String, profileContactId: String, isContact: Boolean){
        if(isContact) {
            repository.findOneByIdProfileAndIdProfileOfContact(profileId, profileContactId)
                ?.let {
                    throw DomainException(ProviderExceptions.DB_CONTACT_ALREADY_EXISTS)
                } ?:let{
                repository.save(
                    DBContact(
                        idProfile = profileId,
                        idProfileOfContact = profileContactId
                    )
                )
            }
        } else {
            repository.findOneByIdProfileAndIdProfileOfContact(profileId, profileContactId)
                ?.let {
                    repository.delete(it)
                } ?:let{
                throw DomainException(ProviderExceptions.DB_CONTACT_NOT_FOUND)
            }
        }
    }
}