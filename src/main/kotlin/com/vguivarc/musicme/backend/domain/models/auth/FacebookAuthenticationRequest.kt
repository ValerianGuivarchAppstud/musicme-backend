package com.vguivarc.musicme.backend.domain.models.auth

import com.vguivarc.musicme.backend.domain.models.profile.Profile
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

class FacebookAuthenticationRequest(token: String) :
    UsernamePasswordAuthenticationToken(token, token)