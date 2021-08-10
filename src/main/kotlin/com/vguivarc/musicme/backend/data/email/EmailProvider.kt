package com.vguivarc.musicme.backend.data.email

import com.vguivarc.musicme.backend.domain.providers.email.IEmailProvider
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

@Service
class EmailProvider(
    val properties: EmailProperties,
    val mailSender: JavaMailSender
) : IEmailProvider {
    override fun sendMail(email: String, body: String, subject: String) {
        if (properties.active) {
            val mimeMessage = mailSender.createMimeMessage()
            val message = MimeMessageHelper(mimeMessage, true, "UTF-8")
            message.setTo(email)
            message.setFrom(properties.from)
            message.setSubject(subject)
            message.setText(body)
            mailSender.send(mimeMessage)
        }
        return
    }
}
