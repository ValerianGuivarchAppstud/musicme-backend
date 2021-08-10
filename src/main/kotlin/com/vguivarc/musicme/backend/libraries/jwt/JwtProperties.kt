package com.vguivarc.musicme.backend.libraries.jwt

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
@ConfigurationProperties("application.libraries.security.jwt", ignoreUnknownFields = false)
class JwtProperties {

    var enabled: Boolean = false

    lateinit var secretKey: String

    // 1 hour
    var timeToLive: Long = TimeUnit.HOURS.toMillis(1)

    // 30 days
    @Suppress("MagicNumber")
    var rememberTimeToLive: Long = TimeUnit.DAYS.toMillis(30L)
}
