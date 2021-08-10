package com.vguivarc.musicme.backend.libraries.security

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class PasswordEncoderConfiguration {

    @Bean
    @ConditionalOnProperty("application.libraries.security.bcrypt.enabled", matchIfMissing = true, havingValue = "true")
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}
