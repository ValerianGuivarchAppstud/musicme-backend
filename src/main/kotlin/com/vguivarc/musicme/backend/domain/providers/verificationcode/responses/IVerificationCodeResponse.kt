package com.vguivarc.musicme.backend.domain.providers.verificationcode.responses

import com.vguivarc.musicme.backend.domain.models.VerificationCode

interface IVerificationCodeResponse {
    fun toVerificationCode(): VerificationCode
}
