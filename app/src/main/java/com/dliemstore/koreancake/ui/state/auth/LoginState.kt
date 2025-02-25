package com.dliemstore.koreancake.ui.state.auth

data class LoginState(
    val username: String = "",
    val password: String = "",
    val errors: Map<String, String?> = emptyMap(),
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val statusCode: Int? = null,
    val errorMessage: String? = null
)
