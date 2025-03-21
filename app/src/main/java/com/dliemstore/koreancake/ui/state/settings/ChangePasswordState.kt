package com.dliemstore.koreancake.ui.state.settings

data class ChangePasswordState(
    val oldPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val errors: Map<String, String?> = emptyMap(),
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val statusCode: Int? = null,
    val errorMessage: String? = null
)
