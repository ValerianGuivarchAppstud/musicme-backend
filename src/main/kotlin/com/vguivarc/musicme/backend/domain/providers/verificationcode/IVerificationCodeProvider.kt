package com.vguivarc.musicme.backend.domain.providers.verificationcode

import com.vguivarc.musicme.backend.domain.providers.verificationcode.responses.IVerificationCodeResponse

interface IVerificationCodeProvider {

    fun addVerificationCode(accountId: String): IVerificationCodeResponse

    fun addVerificationCodeWithMail(accountId: String, email: String): IVerificationCodeResponse

    fun findByAccount(accountId: String): List<IVerificationCodeResponse>

    fun markAsUsed(codeId: String): IVerificationCodeResponse
}
