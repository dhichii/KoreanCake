package com.dliemstore.koreancake.domain.validation.auth

import com.dliemstore.koreancake.util.RegexUtils
import com.dliemstore.koreancake.util.ValidationHelper

object LoginValidator {
    fun validate(
        username: String,
        password: String
    ): Map<String, String?> {
        return mapOf(
            "username" to listOfNotNull(
                ValidationHelper.required(username, "Username"),
                ValidationHelper.minLength(username, 3, "Username"),
                if (!RegexUtils.isValidUsername(username)) "Username hanya boleh berupa huruf dan angka." else null
            ).firstOrNull(),
            "password" to listOfNotNull(
                ValidationHelper.required(password, "Password"),
                ValidationHelper.minLength(password, 8, "Password")
            ).firstOrNull()
        ).filterValues { it != null }
    }
}