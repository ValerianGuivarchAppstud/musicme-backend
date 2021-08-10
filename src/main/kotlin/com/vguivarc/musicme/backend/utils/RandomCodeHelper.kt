package com.vguivarc.musicme.backend.utils

import java.security.SecureRandom

class RandomCodeHelper {
    companion object {
        @Suppress("MagicNumber")
        fun generateRandomCode(length: Int): String {
            return List(length) { SecureRandom().nextInt(10) }.joinToString("")
        }
    }
}
