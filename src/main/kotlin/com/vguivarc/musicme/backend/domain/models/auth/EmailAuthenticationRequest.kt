package com.vguivarc.musicme.backend.domain.models.auth

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

class EmailAuthenticationRequest(email: String, code: String) :
    UsernamePasswordAuthenticationToken(email, code)
