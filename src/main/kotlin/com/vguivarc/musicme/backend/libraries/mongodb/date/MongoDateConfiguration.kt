package com.vguivarc.musicme.backend.libraries.mongodb.date

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.data.mongodb.core.convert.MongoCustomConversions
import java.time.ZoneId.systemDefault
import java.time.ZonedDateTime
import java.time.ZonedDateTime.ofInstant
import java.util.Date

@Configuration
class MongoDateConfiguration {

    @Bean
    fun customConversions(): MongoCustomConversions {
        val converters = ArrayList<Converter<*, *>>()
        converters.add(DateToZonedDateTimeConverter.INSTANCE)
        converters.add(ZonedDateTimeToDateConverter.INSTANCE)
        return MongoCustomConversions(converters)
    }

    internal enum class DateToZonedDateTimeConverter : Converter<Date, ZonedDateTime> {

        INSTANCE;

        override fun convert(source: Date): ZonedDateTime {
            return ofInstant(source.toInstant(), systemDefault())
        }
    }

    internal enum class ZonedDateTimeToDateConverter : Converter<ZonedDateTime, Date> {

        INSTANCE;

        override fun convert(source: ZonedDateTime): Date {
            return Date.from(source.toInstant())
        }
    }
}
