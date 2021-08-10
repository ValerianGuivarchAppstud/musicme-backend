package com.vguivarc.musicme.backend.domain.models.auth

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

class PasswordAuthenticationRequest(email: String, password: String) :
    UsernamePasswordAuthenticationToken(email, password)
