package com.vguivarc.musicme.backend.domain.providers.email

interface IEmailProvider {

    fun sendMail(email: String, body: String, subject: String)
}
