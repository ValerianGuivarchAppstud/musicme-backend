package com.vguivarc.musicme.backend.config.migrations

import com.github.mongobee.changeset.ChangeLog
import com.github.mongobee.changeset.ChangeSet
import org.springframework.data.mongodb.core.MongoTemplate

@ChangeLog(order = "001")
class Migration001Initialization {

    @Suppress("EmptyFunctionBlock")
    @ChangeSet(order = "2020-08-04-10-00", author = "initiator", id = "2020-08-04-10-00-init")
    fun init(mongoTemplate: MongoTemplate) { }
}
