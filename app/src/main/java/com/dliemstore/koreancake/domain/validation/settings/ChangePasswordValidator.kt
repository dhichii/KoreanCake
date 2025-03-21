package com.dliemstore.koreancake.domain.validation.settings

import com.dliemstore.koreancake.util.ValidationHelper

object ChangePasswordValidator {
    fun validate(
        oldPassword: String,
        newPassword: String,
        confirmPassword: String
    ): Map<String, String?> {
        return mapOf(
            "oldPassword" to listOfNotNull(
                ValidationHelper.required(oldPassword, "Password Sekarang"),
                ValidationHelper.minLength(oldPassword, 8, "Password Sekarang")
            ).firstOrNull(),
            "newPassword" to listOfNotNull(
                ValidationHelper.required(newPassword, "Password Baru"),
                ValidationHelper.minLength(newPassword, 8, "Password Baru"),
                if (newPassword == oldPassword) "Password baru tidak boleh sama dengan password lama" else null
            ).firstOrNull(),
            "confirmPassword" to listOfNotNull(
                ValidationHelper.required(confirmPassword, "Konfirmasi password"),
                if (newPassword != confirmPassword) "Konfirmasi password tidak sama" else null,
                ValidationHelper.minLength(confirmPassword, 8, "Konfirmasi password")
            ).firstOrNull()
        ).filterValues { it != null }
    }
}