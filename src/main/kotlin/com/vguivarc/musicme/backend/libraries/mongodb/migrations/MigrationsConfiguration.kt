package com.vguivarc.musicme.backend.libraries.mongodb.migrations

import com.github.mongobee.Mongobee
import com.mongodb.MongoClient
import com.vguivarc.musicme.backend.libraries.mongodb.MongoDBProperties
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.BeanInitializationException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.mongo.MongoProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate

@ConditionalOnProperty("application.libraries.mongodb.migrations.enabled")
@Configuration
class MigrationsConfiguration {

    val log: Logger = LoggerFactory.getLogger(MigrationsConfiguration::class.java)

    @Autowired
    lateinit var properties: MongoDBProperties

    @Bean
    fun mongobee(mongoClient: MongoClient, mongoTemplate: MongoTemplate, mongoProperties: MongoProperties): Mongobee {
        log.debug("Configuring Mongobee")

        if (properties.migrations.migrationPackage.isEmpty())
            throw BeanInitializationException(
                "Package name is empty." +
                    " Please provide value in 'application.libraries.mongodb.migrations.package'"
            )

        val mongobee = Mongobee(mongoClient)
        mongobee.setDbName(mongoProperties.database ?: "swag-dev")
        mongobee.setMongoTemplate(mongoTemplate)
        // package to scan for migrations
        mongobee.setChangeLogsScanPackage(properties.migrations.migrationPackage)
        mongobee.isEnabled = true
        return mongobee
    }
}
