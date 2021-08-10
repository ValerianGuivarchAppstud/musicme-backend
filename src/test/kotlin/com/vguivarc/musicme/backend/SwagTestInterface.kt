package com.vguivarc.musicme.backend

import com.fasterxml.jackson.databind.ObjectMapper
import com.thedeanda.lorem.LoremIpsum
import com.vguivarc.musicme.backend.data.database.accounts.DBAccountRepository
import com.vguivarc.musicme.backend.data.database.verificationcodes.DBVerificationCode
import com.vguivarc.musicme.backend.data.database.verificationcodes.DBVerificationCodeRepository
import com.vguivarc.musicme.backend.domain.models.VerificationCode
import com.vguivarc.musicme.backend.domain.models.account.Account
import com.vguivarc.musicme.backend.domain.models.nested.AccountStatus
import com.vguivarc.musicme.backend.domain.models.nested.Authority
import com.vguivarc.musicme.backend.domain.providers.account.IAccountProvider
import com.vguivarc.musicme.backend.domain.providers.email.IEmailProvider
import com.vguivarc.musicme.backend.testhelpers.MongoInitializer
import com.vguivarc.musicme.backend.web.api.v1.client.auth.entities.JwtAuthResponseVM
import com.vguivarc.musicme.backend.web.api.v1.client.auth.requests.LoginWithDeviceRequest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.Extensions
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import java.time.ZonedDateTime

@SpringBootTest
@ContextConfiguration(initializers = [MongoInitializer::class])
@ActiveProfiles(profiles = ["test"])
@Extensions(
    ExtendWith(RestDocumentationExtension::class),
    ExtendWith(SpringExtension::class)
)
// @AutoConfigureWireMock(
//    port = 8081,
//    stubs = ["classpath:wiremock/**/mappings/**/*.json"],
//    files = ["classpath:wiremock"]
// )
abstract class SwagTestInterface {

    val logger: Logger = LoggerFactory.getLogger(SwagTestInterface::class.java)

    // Repositories
    @Autowired
    lateinit var dBAccountRepository: DBAccountRepository

    @Autowired
    lateinit var dbVerificationCodeRepository: DBVerificationCodeRepository

    // Utils
    @Autowired
    lateinit var context: WebApplicationContext

    @Autowired
    lateinit var mapper: ObjectMapper

    // Providers
    @Autowired
    lateinit var accountProvider: IAccountProvider

    // Methods
    // Mocks
    @MockBean
    lateinit var emailProvider: IEmailProvider

    val lorem = LoremIpsum.getInstance()
    private var accountClientId = ""
    private var accountProfessionalId = ""

    lateinit var mvc: MockMvc

    @BeforeEach
    fun before(restDocumentation: RestDocumentationContextProvider) {
        Mockito.`when`(emailProvider.sendMail(anyString(), anyString(), anyString()))
            .then {
                logger.info("mock email sent")
            }

        this.mvc = MockMvcBuilders.webAppContextSetup(context)
            .apply<DefaultMockMvcBuilder>(SecurityMockMvcConfigurers.springSecurity())
            .build()

        this.addTestAccountClient()
        this.addTestAccountProfessional()
    }

    @AfterEach
    fun after() {
        this.cleanUp()
    }

    fun getClientAccessToken(): JwtAuthResponseVM {
        val response = this.mvc.perform(
            MockMvcRequestBuilders.post("/api/v1/client/auth/login/device")
                .content(
                    mapper.writeValueAsString(
                        LoginWithDeviceRequest(
                            getTestAccountClient().deviceId!!
                        )
                    )
                )
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn().response.contentAsByteArray

        val json = this.mapper.readTree(response)

        return JwtAuthResponseVM(
            json.get("access_token").asText(),
            json.get("access_token_expiration").asLong(),
            json.get("refresh_token").asText(),
            json.get("refresh_token_expiration").asLong()
        )
    }

    fun addTestAccountClient(): Account {
        val account = accountProvider.create(
            Account(
                deviceId = "client@test.com",
                // email = "client@test.com",
                phone = "0203040506",
                authority = Authority.USER,
                status = AccountStatus.ACTIVE
            )
        ).toAccount()
        accountClientId = account.id
        return account
    }

    fun getTestAccountClient(): Account {
        return accountProvider.findOneById(accountClientId).toAccount()
    }

    fun addTestAccountProfessional(): Account {
        val account = accountProvider.create(
            Account(
                deviceId = "pro@test.com",
                email = "pro@test.com",
                phone = "0606060606",
                password = "password-pro",
                authority = Authority.USER,
                status = AccountStatus.ACTIVE
            )
        ).toAccount()
        accountProfessionalId = account.id
        return account
    }

    fun addVerificationCode(): VerificationCode {
        return dbVerificationCodeRepository
            .save(
                DBVerificationCode(
                    code = "1234",
                    expirationDate = ZonedDateTime.now().plusDays(1),
                    accountId = getTestAccountClient().id,
                    isUsed = false
                )
            ).toVerificationCode()
    }

    fun MockHttpServletRequestBuilder.addAuthToken(accessToken: String): MockHttpServletRequestBuilder {
        return this.header("Authorization", "Bearer $accessToken")
    }

    fun cleanUp() {
        dBAccountRepository.deleteAll()
        dbVerificationCodeRepository.deleteAll()
    }
}
