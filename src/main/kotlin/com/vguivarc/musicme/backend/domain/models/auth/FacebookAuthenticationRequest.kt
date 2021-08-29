package com.vguivarc.musicme.backend.domain.models.auth

import com.vguivarc.musicme.backend.domain.models.profile.Profile
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.authentication.AbstractAuthenticationToken

class FacebookAuthenticationRequest(authorities: List<GrantedAuthority>, private val profile: Profile) :
    AbstractAuthenticationToken(authorities) {
    override fun getCredentials(): Any {
        return profile
    }

    override fun getPrincipal(): Any {
        return profile
    }
}