package com.vguivarc.musicme.backend.data.database.verificationcodes

import com.vguivarc.musicme.backend.config.constants.ApplicationConstants
import com.vguivarc.musicme.backend.domain.providers.verificationcode.IVerificationCodeProvider
import com.vguivarc.musicme.backend.domain.providers.verificationcode.responses.IVerificationCodeResponse
import com.vguivarc.musicme.backend.utils.RandomCodeHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.ZonedDateTime

@Service
class DBVerificationCodeProvider : IVerificationCodeProvider {

    @Autowired
    lateinit var repository: DBVerificationCodeRepository

    override fun addVerificationCode(accountId: String): IVerificationCodeResponse {
        val code = DBVerificationCode(
            id = null,
            code = RandomCodeHelper.generateRandomCode(ApplicationConstants.ACCOUNT_VERIFICATION_CODE_SIZE),
            expirationDate = ZonedDateTime.now().plusDays(1),
            accountId = accountId
        )
        return repository.save(code)
    }

    override fun addVerificationCodeWithMail(accountId: String, email: String): IVerificationCodeResponse {
        val code = DBVerificationCode(
            id = null,
            code = RandomCodeHelper.generateRandomCode(ApplicationConstants.ACCOUNT_VERIFICATION_CODE_SIZE),
            expirationDate = ZonedDateTime.now().plusDays(1),
            accountId = accountId,
            email = email
        )
        return repository.save(code)
    }

    override fun findByAccount(accountId: String): List<IVerificationCodeResponse> {
        return repository.findByAccountId(accountId)
    }

    override fun markAsUsed(codeId: String): IVerificationCodeResponse {
        val verificationCode = repository.findOneById(codeId)
        verificationCode.isUsed = true
        return repository.save(verificationCode)
    }
}
