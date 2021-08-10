package com.vguivarc.musicme.backend.domain.models.auth

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

class DeviceAuthenticationRequest(deviceId: String) :
    UsernamePasswordAuthenticationToken(deviceId, deviceId)
