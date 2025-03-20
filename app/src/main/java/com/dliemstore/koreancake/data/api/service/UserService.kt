package com.dliemstore.koreancake.data.api.service

import com.dliemstore.koreancake.data.source.remote.request.user.UpdateEmailRequest
import com.dliemstore.koreancake.data.source.remote.request.user.UpdatePasswordRequest
import com.dliemstore.koreancake.data.source.remote.request.user.UpdateProfileRequest
import com.dliemstore.koreancake.data.source.remote.request.user.UpdateUsernameRequest
import com.dliemstore.koreancake.data.source.remote.response.SuccessResponse
import com.dliemstore.koreancake.data.source.remote.response.user.UserProfileResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.PUT

interface UserService {
    @GET("users/profile")
    suspend fun getProfile(): Response<SuccessResponse<UserProfileResponse>>

    @PUT("users/profile")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): Response<Unit>

    @PATCH("users/email")
    suspend fun updateEmail(@Body request: UpdateEmailRequest): Response<Unit>

    @PATCH("users/username")
    suspend fun updateUsername(@Body request: UpdateUsernameRequest): Response<Unit>

    @PATCH("users/password")
    suspend fun updatePassword(@Body request: UpdatePasswordRequest): Response<Unit>
}