package com.dliemstore.koreancake.data.source.remote.request.auth

data class RegisterRequest(
    val name: String,
    val email: String,
    val username: String,
    val password: String
)
