package com.vguivarc.musicme.backend.domain.models.account

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

class AccountAuthentication(account: Account) :
    UsernamePasswordAuthenticationToken(account.idAccount, account.secret)
