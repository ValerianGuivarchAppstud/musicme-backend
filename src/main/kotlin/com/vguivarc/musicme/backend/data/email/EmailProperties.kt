package com.vguivarc.musicme.backend.data.email

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("data.email", ignoreUnknownFields = false)
class EmailProperties {

    var active: Boolean = false

    lateinit var from: String
}
