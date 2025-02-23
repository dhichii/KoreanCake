package com.dliemstore.koreancake.domain.validation

import com.dliemstore.koreancake.util.RegexUtils
import com.dliemstore.koreancake.util.ValidationHelper

object RegisterValidator {
    fun validate(
        name: String,
        email: String,
        username: String,
        password: String,
        confirmPassword: String
    ): Map<String, String?> {
        return mapOf(
            "name" to ValidationHelper.required(name, "Nama"),
            "username" to listOfNotNull(
                ValidationHelper.required(username, "Username"),
                ValidationHelper.minLength(username, 3, "Username"),
                if (!RegexUtils.isValidUsername(username)) "Username hanya boleh berupa huruf dan angka." else null
            ).firstOrNull(),
            "email" to listOfNotNull(
                ValidationHelper.required(email, "Email"),
                if (!RegexUtils.isValidEmail(email)) "Email tidak valid." else null
            ).firstOrNull(),
            "password" to listOfNotNull(
                ValidationHelper.required(password, "Password"),
                ValidationHelper.minLength(password, 8, "Password")
            ).firstOrNull(),
            "confirmPassword" to listOfNotNull(
                ValidationHelper.required(password, "Konfirmasi password"),
                if (password != confirmPassword) "Konfirmasi password tidak sama" else null,
                ValidationHelper.minLength(password, 8, "Konfirmasi password")
            ).firstOrNull()
        ).filterValues{ it != null }
    }
}