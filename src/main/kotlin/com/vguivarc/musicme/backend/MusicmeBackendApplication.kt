package com.vguivarc.musicme.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MusicmeBackendApplication

@Suppress("SpreadOperator")
fun main(args: Array<String>) {
    runApplication<MusicmeBackendApplication>(*args)
}
