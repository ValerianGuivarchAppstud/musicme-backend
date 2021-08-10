package com.vguivarc.musicme.backend.libraries.entities

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class Updatable(
    val nullable: Boolean = false,
    val inception: Boolean = false
)
