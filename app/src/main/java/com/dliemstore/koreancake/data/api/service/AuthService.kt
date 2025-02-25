package com.dliemstore.koreancake.data.api.service

import com.dliemstore.koreancake.data.source.remote.request.auth.LoginRequest
import com.dliemstore.koreancake.data.source.remote.request.auth.RegisterRequest
import com.dliemstore.koreancake.data.source.remote.response.SuccessResponse
import com.dliemstore.koreancake.data.source.remote.response.auth.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<Unit>

    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<SuccessResponse<TokenResponse>>

    @POST("auth/refresh")
    suspend fun refresh(): Response<SuccessResponse<TokenResponse>>
}