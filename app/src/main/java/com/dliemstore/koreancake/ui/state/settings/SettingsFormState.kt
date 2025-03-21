package com.dliemstore.koreancake.ui.state.settings

data class SettingsFormState(
    val input: String = "",
    val error: String? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val statusCode: Int? = null,
    val errorMessage: String? = null
)
