package com.vguivarc.musicme.backend.utils

import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun ZonedDateTime.toIsoDateTimeFormat(): String {
    return this.withZoneSameInstant(ZoneId.of("UTC")).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
}

// this is to display future dates with the correct time offset
fun ZonedDateTime.toFormatPreserveTimezone(format: String): String {
    return this.withZoneSameInstant(ZoneId.of("Europe/Paris")).format(DateTimeFormatter.ofPattern(format))
}
