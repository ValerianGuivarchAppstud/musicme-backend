package com.vguivarc.musicme.backend.web.api.v1.client.profile

import com.vguivarc.musicme.backend.SwagTestInterface
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

class ClientProfileControllerTest : SwagTestInterface() {

    val testBaseEndpointProfile = "/api/v1/client/profile"

    /**
     * PROFILE
     */
    @Test
    fun `Should return client - OK`() {
        val accessToken = getClientAccessToken()

        this.mvc.perform(
            MockMvcRequestBuilders.get("$testBaseEndpointProfile/me")
                .addAuthToken(accessToken.accessToken)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.accountId").value(this.getTestAccountClient().id))
            // .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(this.getTestAccountClient().email!!))
            .andExpect(MockMvcResultMatchers.jsonPath("$.deviceId").value(this.getTestAccountClient().deviceId!!))
            .andExpect(MockMvcResultMatchers.jsonPath("$.uuid").value(this.getTestAccountClient().uuid!!))
    }

    // TODO FIX ME
    /*@Test
    @Ignore
    fun should_return_profile_client_new_email_SUCCESS() {
        val newEmail = "${UUID.randomUUID()}@test.com"
        val accessToken = getClientAccessToken()

        this.mvc.perform(
            put("$testBaseEndpointProfile")
                .header("Authorization", "Bearer ${accessToken.accessToken}")
                .content(
                    mapper.writeValueAsString(
                        UpdateProfileRequest(
                            email = newEmail,
                            firstName = null,
                            lastName = null
                        )
                    )
                )
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "client-profile-PUT"
                )
            )

        this.mvc.perform(
            MockMvcRequestBuilders.get("$testBaseEndpointProfile/me")
                .header("Authorization", "Bearer ${accessToken.accessToken}")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.accountId").value(this.getTestAccountClient().id))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(newEmail))
            .andExpect(MockMvcResultMatchers.jsonPath("$.deviceId").value(this.getTestAccountClient().deviceId!!))
            .andExpect(MockMvcResultMatchers.jsonPath("$.uuid").value(this.getTestAccountClient().uuid!!))
    }*/
}
