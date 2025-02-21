package com.dliemstore.koreancake.data.api.service

import com.dliemstore.koreancake.data.source.remote.request.auth.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<Unit>
}