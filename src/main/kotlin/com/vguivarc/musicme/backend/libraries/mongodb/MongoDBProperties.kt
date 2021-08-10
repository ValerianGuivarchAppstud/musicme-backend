package com.vguivarc.musicme.backend.libraries.mongodb

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("application.libraries.mongodb", ignoreUnknownFields = true)
class MongoDBProperties {

    var enabled: Boolean = false

    var dates: DatesProperties = DatesProperties()

    var migrations: MigrationsProperties = MigrationsProperties()

    class DatesProperties {

        var enabled: Boolean = false
    }

    class MigrationsProperties {

        var enabled: Boolean = false

        var migrationPackage: String = ""
    }
}
