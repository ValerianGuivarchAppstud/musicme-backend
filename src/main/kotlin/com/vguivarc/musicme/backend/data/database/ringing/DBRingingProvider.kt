package com.vguivarc.musicme.backend.data.database.ringing

import com.vguivarc.musicme.backend.domain.models.contact.Contact
import com.vguivarc.musicme.backend.domain.models.ringing.Ringing
import com.vguivarc.musicme.backend.domain.models.ringing.RingingState
import com.vguivarc.musicme.backend.domain.models.song.Song
import com.vguivarc.musicme.backend.domain.providers.ringing.IRingingProvider
import com.vguivarc.musicme.backend.domain.providers.ringing.responses.IRingingResponse
import com.vguivarc.musicme.backend.libraries.entities.EntityUtils
import com.vguivarc.musicme.backend.libraries.errors.DomainException
import com.vguivarc.musicme.backend.libraries.errors.ProviderExceptions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DBRingingProvider : IRingingProvider {

    @Autowired
    lateinit var repository: DBRingingRepository

    override fun sendRinging(idProfile: String, idProfileOfContact: String, song: Song): Ringing {
        return repository.save(DBRinging(
            song = song,
            state = RingingState.WAITING,
            senderId = idProfile,
            receiverId = idProfileOfContact
        )).toRinging()
    }

    override fun listenRinging(ringingId: String) {
            repository.findOneById(ringingId)?.let {
                val listenedCopy = it.copy(state = RingingState.LISTENED)
                repository.save(EntityUtils.update(it, listenedCopy))
            }?:run {
            throw DomainException(ProviderExceptions.DB_RINGING_NOT_FOUND)
        }
    }

    override fun getNextRinging(id: String): Ringing? {
        return repository.findAllByReceiverId(id).maxBy { it.toRinging().createdDate }?.toRinging()
    }

    override fun getWaitingRingingList(id: String): List<IRingingResponse> {
        return repository.findAllByReceiverId(id).filter { it.toRinging().state==RingingState.WAITING }
    }

    override fun getListRingingListened(id: String): List<Ringing> {
        return repository.findAllByReceiverId(id).filter { it.toRinging().state==RingingState.LISTENED }
            .map { it.toRinging() }
    }

    override fun getNbRingingSent(contact: Contact): Int {
        return repository.findAllBySenderIdAndReceiverId(contact.idProfile, contact.idProfileOfContact).size
    }

    override fun getNbRingingReceived(contact: Contact): Int {
        return repository.findAllBySenderIdAndReceiverId(contact.idProfileOfContact, contact.idProfile).size
    }
}