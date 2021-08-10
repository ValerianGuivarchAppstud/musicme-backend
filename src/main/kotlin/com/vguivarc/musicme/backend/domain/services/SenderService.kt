package com.vguivarc.musicme.backend.domain.services

import com.vguivarc.musicme.backend.domain.models.VerificationCode
import com.vguivarc.musicme.backend.domain.providers.email.IEmailProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SenderService {

    @Autowired
    lateinit var emailProvider: IEmailProvider

    fun sendVerificationCodeByMail(mail: String, code: VerificationCode) {
        return emailProvider.sendMail(
            mail, "CODE: " + code.code,
            "Code de vérification"
        )
    }
}
