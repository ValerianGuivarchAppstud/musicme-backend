package com.vguivarc.musicme.backend

import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.cloud.contract.wiremock.WireMockSpring
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.core.env.Environment

@TestConfiguration
class WiremockConfig {

    @Autowired
    private lateinit var environment: Environment

    @Bean
    @Primary
    fun wireMockOptions(): WireMockConfiguration {
        val options = WireMockSpring.options()
        options.extensions(ResponseTemplateTransformer(true))
        options.port(this.environment.getProperty("wiremock.server.port")!!.toInt())
        return options
    }
}
