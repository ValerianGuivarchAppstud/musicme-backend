package com.vguivarc.musicme.backend.testhelpers

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.MongoDBContainer

class MongoInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
    override fun initialize(context: ConfigurableApplicationContext) {
        val addedProperties = listOf(
            "spring.data.mongodb.uri=${MongoContainerSingleton.instance.replicaSetUrl}"
        )
        TestPropertyValues.of(addedProperties).applyTo(context.environment)
    }
}

object MongoContainerSingleton {
    val instance: MongoDBContainer by lazy { startMongoContainer() }
    private fun startMongoContainer(): MongoDBContainer =
        MongoDBContainer("mongo:4.4.3-bionic")
            .withReuse(true)
            .apply { start() }
}
