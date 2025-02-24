package com.dliemstore.koreancake.ui.state.auth

data class RegisterState(
    val name: String = "",
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val errors: Map<String, String?> = emptyMap(),
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)
