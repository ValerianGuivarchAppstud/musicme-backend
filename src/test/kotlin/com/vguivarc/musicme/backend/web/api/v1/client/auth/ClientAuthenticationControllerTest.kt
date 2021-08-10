package com.vguivarc.musicme.backend.web.api.v1.client.auth

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.vguivarc.musicme.backend.SwagTestInterface
import com.vguivarc.musicme.backend.domain.models.nested.AccountStatus
import com.vguivarc.musicme.backend.web.api.v1.client.auth.requests.*
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import java.util.*

class ClientAuthenticationControllerTest : SwagTestInterface() {

    val testBaseEndpointAuth = "/api/v1/client/auth"
    val testBaseEndpointProfile = "/api/v1/client/profile"

    /**
     * REGISTER
     */
    @Test
    fun `Should return registered account client - SUCCESS`() {
        val req = RegisterWithPasswordRequest(
            email = lorem.email,
            password = lorem.getWords(3)
        )

        this.mvc.perform(
            post("$testBaseEndpointAuth/register")
                .content(mapper.writeValueAsString(req))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(jsonPath("$.email").value(req.email))
            .andExpect(jsonPath("$.status").value(AccountStatus.NEW.toString()))
    }

    @Test
    fun `Should return conflict - ERROR`() {
        val req = RegisterWithPasswordRequest(
            email = lorem.email,
            password = lorem.getWords(3)
        )

        this.mvc.perform(
            post("$testBaseEndpointAuth/register")
                .content(mapper.writeValueAsString(req))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isCreated)

        this.mvc.perform(
            post("$testBaseEndpointAuth/register")
                .content(mapper.writeValueAsString(req))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isConflict)
    }

    @Test
    fun `Should return authenticated account client by password - SUCCESS`() {
        val req = RegisterWithPasswordRequest(
            email = lorem.email,
            password = lorem.getWords(3)
        )
        this.mvc.perform(
            post("$testBaseEndpointAuth/register")
                .content(mapper.writeValueAsString(req))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isCreated)

        val reqLogin = LoginWithPasswordRequest(
            email = req.email,
            password = req.password
        )
        this.mvc.perform(
            post("$testBaseEndpointAuth/login")
                .content(mapper.writeValueAsString(reqLogin))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `Should return registered account client with deviceId - SUCCESS`() {
        val req = RegisterWithDeviceRequest(
            deviceId = UUID.randomUUID().toString()
        )

        this.mvc.perform(
            post("$testBaseEndpointAuth/register/device")
                .content(mapper.writeValueAsString(req))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(jsonPath("$.deviceId").value(req.deviceId))
            .andExpect(jsonPath("$.status").value(AccountStatus.NEW.toString()))
    }

    @Test
    fun `Should return registered account client - FAILURE`() {
        val req = RegisterWithDeviceRequest(
            deviceId = getTestAccountClient().deviceId!!
        )

        this.mvc.perform(
            post("$testBaseEndpointAuth/register/device")
                .content(mapper.writeValueAsString(req))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isConflict)
    }

    /**
     * LOGIN BY DEVICE
     */
    @Test
    fun `Should return authenticated account client by device - SUCCESS`() {
        this.mvc.perform(
            post("$testBaseEndpointAuth/login/device")
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
    }

    @Test
    fun `Should return active client - SUCCESS`() {
        val deviceId = UUID.randomUUID().toString()
        this.mvc.perform(
            post("$testBaseEndpointAuth/register/device")
                .content(
                    mapper.writeValueAsString(
                        RegisterWithDeviceRequest(
                            deviceId
                        )
                    )
                )
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(jsonPath("$.deviceId").value(deviceId))
            .andReturn().response.contentAsByteArray

        val response = this.mvc.perform(
            post("$testBaseEndpointAuth/login/device")
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

        this.mvc.perform(
            get("$testBaseEndpointProfile/me")
                .addAuthToken(json.get("access_token").asText())
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(jsonPath("$.deviceId").value(this.getTestAccountClient().deviceId!!))
            .andExpect(jsonPath("$.status").value(AccountStatus.ACTIVE.toString()))
    }

    @Test
    fun `Should return authenticated account client by device - FAILURE`() {
        val deviceId = UUID.randomUUID().toString()
        this.mvc.perform(
            post("$testBaseEndpointAuth/login/device")
                .content(
                    mapper.writeValueAsString(
                        LoginWithDeviceRequest(
                            deviceId
                        )
                    )
                )
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    /**
     * UNAUTHENTICATED CALLS
     */
    @Test
    fun `Should return authenticated account - FAILURE`() {
        this.mvc.perform(
            get("$testBaseEndpointAuth/me")
                .addAuthToken("Bearer wrongtoken")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().is4xxClientError)
    }

    @Test
    fun `Should refresh token`() {
        val json = getClientAccessToken()
        this.mvc.perform(
            post("$testBaseEndpointAuth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    jacksonObjectMapper().writeValueAsString(
                        JwtRefreshAuthRequest(
                            json.accessToken,
                            json.refreshToken!!
                        )
                    )
                )
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(jsonPath("$.access_token").exists())
            .andExpect(jsonPath("$.refresh_token").exists())
    }

    @Test
    fun `Should not refresh token - FAILURE`() {
        val json = getClientAccessToken()
        this.mvc.perform(
            post("$testBaseEndpointAuth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    jacksonObjectMapper().writeValueAsString(
                        JwtRefreshAuthRequest(
                            json.accessToken,
                            "wrong token"
                        )
                    )
                )
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isForbidden)
    }
}
