package com.dliemstore.koreancake.domain.validation.settings

import com.dliemstore.koreancake.util.ValidationHelper

object UpdateProfileValidator {
    fun validate(input: String): String? {
        return ValidationHelper.required(input, "Nama")
    }
}