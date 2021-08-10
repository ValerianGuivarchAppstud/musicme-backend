package com.vguivarc.musicme.backend.utils

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

const val API_FORMAT: String = "yyyy-MM-dd'T'HH:mm:ssZ"

class ZonedDateTimeJsonSerializer : JsonSerializer<ZonedDateTime>() {

    private fun toAPIFormat(dateTime: ZonedDateTime?): String {
        if (dateTime == null)
            return ""
        val formatter = DateTimeFormatter.ofPattern(API_FORMAT)
        return dateTime.format(formatter)
    }

    override fun serialize(value: ZonedDateTime?, jsonGenerator: JsonGenerator?, serializers: SerializerProvider?) {
        jsonGenerator?.writeObject(toAPIFormat(value))
    }
}

class ZonedDateTimeJsonDeserializer : JsonDeserializer<ZonedDateTime>() {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): ZonedDateTime? {
        val oc = p?.codec
        oc?.let {
            val node: JsonNode = oc.readTree(p)
            val formatter = DateTimeFormatter.ofPattern(API_FORMAT)
            return ZonedDateTime.parse(node.textValue(), formatter)
        }
        return null
    }
}
