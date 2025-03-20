package com.dliemstore.koreancake.data.source.remote.response.user

data class UserProfileResponse(
    val id: String,
    val name: String,
    val username: String,
    val email: String,
    val role: String,
    val createdAt: String,
    val updatedAt: String
)
