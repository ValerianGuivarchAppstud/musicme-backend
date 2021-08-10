package com.vguivarc.musicme.backend.domain.models.auth

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class AccessToken(
    principal: Any,
    credentials: Any,
    authorities: List<GrantedAuthority>
) : UsernamePasswordAuthenticationToken(principal, credentials, authorities)
