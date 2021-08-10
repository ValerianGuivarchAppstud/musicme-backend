package com.vguivarc.musicme.backend.config.configurations

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.web.filter.CommonsRequestLoggingFilter

@Suppress("MagicNumber")
@Profile(value = [ "local" ])
@Configuration
class RequestLoggingFilterConfig {
    @Bean
    fun logFilter(): CommonsRequestLoggingFilter {
        val filter = CommonsRequestLoggingFilter()
        filter.setIncludeQueryString(true)
        filter.setIncludePayload(true)
        filter.setMaxPayloadLength(10000)
        filter.setIncludeHeaders(false)
        filter.setBeforeMessagePrefix("Before Request : ")
        filter.setAfterMessagePrefix("After Request : ")
        return filter
    }
}
