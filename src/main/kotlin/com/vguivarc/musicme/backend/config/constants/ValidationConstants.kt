package com.vguivarc.musicme.backend.config.constants

object ValidationConstants {
    // Regex for acceptable logins
    const val MONGOID_REGEX = "^[a-f0-9]{24}$"
    const val VALIDATION_CODE_REGEXP = "^[A-Z0-9]{12}$"
    const val ZIPCODE_REGEX = "^[0-9]{5}(?:-[0-9]{4})?$"
    const val PHONE_REGEX = "^[0-9+ /]*$"
    const val NUMERIC_REGEXP = "^[0-9]*$"
    const val ALPHA_HYPHEN_REGEX = "^[_A-Za-z0-9-]*$"

    val IMAGES_FILES_MIME_TYPE = arrayOf(
        "image/jpeg",
        "image/jpg",
        "image/png",
        "image/bmp"
    )

    /**
     * Account
     */
    const val ACCOUNT_PASSWORD_MIN_LENGTH = 8
    const val ACCOUNT_PASSWORD_MAX_LENGTH = 50
}
