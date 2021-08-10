package com.vguivarc.musicme.backend.config.healtchecks

import com.vguivarc.musicme.backend.domain.providers.account.IAccountProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.actuate.health.AbstractHealthIndicator
import org.springframework.boot.actuate.health.Health
import org.springframework.stereotype.Component

@Component
class MongoHealthIndicator : AbstractHealthIndicator() {

    @Autowired
    lateinit var accountProvider: IAccountProvider

    @Suppress("TooGenericExceptionCaught")
    @Throws(Exception::class)
    override fun doHealthCheck(builder: Health.Builder) {
        // Use the builder to build the health status details that should be reported.
        // If you throw an exception, the status will be DOWN with the exception message.
        try {
            accountProvider.count()
            builder.up()
        } catch (e: Exception) {
            builder.down().withException(e)
        }
    }
}
