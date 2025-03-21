package com.dliemstore.koreancake.domain.validation.settings

import com.dliemstore.koreancake.ui.navigation.graphs.SettingType
import com.dliemstore.koreancake.util.RegexUtils
import com.dliemstore.koreancake.util.ValidationHelper

object SettingsFormValidator {
    fun validate(settingType: SettingType, input: String): String? {
        return when (settingType) {
            is SettingType.Profile -> ValidationHelper.required(input, "Nama")
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
}