package com.vguivarc.musicme.backend.libraries.entities

import org.apache.commons.lang3.reflect.FieldUtils
import org.springframework.beans.PropertyAccessorFactory

object EntityUtils {

    fun <V : Any> update(entity: V, update: V): V {
        val fields = FieldUtils.getFieldsListWithAnnotation(entity::class.java, Updatable::class.java)
        val entityAccessor = PropertyAccessorFactory.forDirectFieldAccess(entity)
        val updateAccessor = PropertyAccessorFactory.forDirectFieldAccess(update)

        for (field in fields) {
            val annotation = field.getAnnotation(Updatable::class.java)
            val updateValue = updateAccessor.getPropertyValue(field.name)
            val currentValue = entityAccessor.getPropertyValue(field.name)

            if (!annotation.nullable && updateValue == null) {
                continue
            }

            if (annotation.inception && currentValue != null && updateValue != null) {
                // we need to go ... deeper ...
                entityAccessor.setPropertyValue(field.name, update(currentValue, updateValue))
            } else {
                entityAccessor.setPropertyValue(field.name, updateValue)
            }
        }
        return entity
    }
}
