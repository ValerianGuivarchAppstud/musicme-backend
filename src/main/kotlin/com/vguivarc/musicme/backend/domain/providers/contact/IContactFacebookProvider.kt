package com.vguivarc.musicme.backend.domain.providers.contact

interface IContactFacebookProvider {
    fun getContactFacebookIdList(socialAuthToken: String): List<String>
}