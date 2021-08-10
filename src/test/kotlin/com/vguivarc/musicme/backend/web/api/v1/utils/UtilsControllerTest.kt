package com.vguivarc.musicme.backend.web.api.v1.utils

import com.vguivarc.musicme.backend.SwagTestInterface
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath

class UtilsControllerTest : SwagTestInterface() {

    val testBaseEndpointUtils = "/api/v1/utils"

    /**
     * REGISTER
     */
    @Test
    fun `Should return build infos - SUCCESS`() {
        this.mvc.perform(
            get("$testBaseEndpointUtils/build_infos")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(jsonPath("$.name").value("swag"))
            .andExpect(jsonPath("$.version").isString)
        // .andExpect(jsonPath("$.buildTime").isString)
    }
}
