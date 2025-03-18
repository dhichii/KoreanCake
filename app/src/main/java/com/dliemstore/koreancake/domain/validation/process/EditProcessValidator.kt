package com.dliemstore.koreancake.domain.validation.process

import com.dliemstore.koreancake.util.ValidationHelper

object EditProcessValidator {
    fun validate(
        name: String,
        step: String
    ): Map<String, String?> {
        return mapOf(
            "name" to listOfNotNull(ValidationHelper.required(name, "Nama")).firstOrNull(),
            "step" to listOfNotNull(
                ValidationHelper.required(step, "Langkah Proses"),
                ValidationHelper.isValidInteger(step, "Langkah Proses"),
                ValidationHelper.minInteger(step, 1, "Langkah Proses"),
            ).firstOrNull()
        ).filterValues { it != null }
    }
}