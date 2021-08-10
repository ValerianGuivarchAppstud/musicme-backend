package com.vguivarc.musicme.backend.config.configurations

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.convert.MongoConverter
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.MongoMappingContext

// this is a class to ensure we are compatible with Spring Data MongoDB 3.x.
// https://docs.spring.io/spring-data/mongodb/docs/2.2.x-SNAPSHOT/reference/html/#mapping-usage
// https://github.com/spring-projects/spring-boot/issues/16337
// https://github.com/spring-projects/spring-boot/issues/19236
@Configuration
class MongoConfiguration {

    val log: Logger = LoggerFactory.getLogger(MongoConfiguration::class.java)

    @Autowired
    lateinit var mongoTemplate: MongoTemplate

    @Autowired
    lateinit var mongoConverter: MongoConverter

    @EventListener(ApplicationReadyEvent::class)
    fun initIndicesAfterStartup() {
        log.info("initIndicesAfterStartup > init")
        val init = System.currentTimeMillis()
        val mappingContext = mongoConverter.mappingContext
        if (mappingContext is MongoMappingContext) {
            mappingContext.persistentEntities.forEach { persistentEntity ->
                val clazz = persistentEntity.type
                if (!clazz.isAnnotationPresent(Document::class.java)) {
                    return@forEach
                }
                val resolver = MongoPersistentEntityIndexResolver(mappingContext)
                val indexOps = mongoTemplate.indexOps(clazz)
                resolver
                    .resolveIndexFor(clazz)
                    .forEach { indexDefinition ->
                        indexOps.ensureIndex(indexDefinition)
                        log.debug(
                            "Mongo InitIndicesAfterStartup ensureIndex > {}",
                            indexDefinition.indexKeys.toJson()
                        )
                    }
            }
        }
        log.info("initIndicesAfterStartup took: {}ms", System.currentTimeMillis() - init)
    }
}
