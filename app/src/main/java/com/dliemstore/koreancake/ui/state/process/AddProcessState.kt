package com.dliemstore.koreancake.ui.state.process

data class AddProcessState(
    val name: String = "",
    val step: String = "",
    val errors: Map<String, String?> = emptyMap(),
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val statusCode: Int? = null,
    val errorMessage: String? = null
)
