package com.dliemstore.koreancake.domain.validation.settings

import com.dliemstore.koreancake.ui.navigation.graphs.SettingType
import com.dliemstore.koreancake.util.RegexUtils
import com.dliemstore.koreancake.util.ValidationHelper

object SettingsFormValidator {
    fun validateInput(settingType: SettingType, input: String): String? {
        return when (settingType) {
            is SettingType.Username -> listOfNotNull(
                ValidationHelper.required(input, "Username"),
                ValidationHelper.minLength(input, 3, "Username"),
                if (!RegexUtils.isValidUsername(input)) "Username hanya boleh berupa huruf dan angka." else null
            ).firstOrNull()

            is SettingType.Email -> listOfNotNull(
                ValidationHelper.required(input, "Email"),
                if (!RegexUtils.isValidEmail(input)) "Email tidak valid." else null
            ).firstOrNull()
        }
    }

    fun validatePassword(password: String): String? {
        return listOfNotNull(
            ValidationHelper.required(password, "Password"),
            ValidationHelper.minLength(password, 8, "Password")
        ).firstOrNull()
    }
}