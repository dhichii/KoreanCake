package com.dliemstore.koreancake.data.source.repository.auth

import com.dliemstore.koreancake.data.api.service.AuthService
import com.dliemstore.koreancake.data.source.remote.request.auth.RegisterRequest
import com.dliemstore.koreancake.util.ApiUtils
import com.dliemstore.koreancake.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authService: AuthService,
) {
    fun register(
        name: String,
        email: String,
        username: String,
        password: String
    ) = flow {
        emit(Resource.Loading())

        try {
            val response =
                authService.register(RegisterRequest(name, email, username, password))
            if (response.isSuccessful) {
                emit(Resource.Success(Unit, response.code()))
            } else {
                val errorResponse = runCatching { ApiUtils.parseError(response) }.getOrNull()
                val errorMessage = when (response.code()) {
                    400 -> if (errorResponse?.message == "Bad Request") {
                        "Registrasi gagal. Mohon periksa kembali."
                    } else errorResponse?.message ?: "Permintaan tidak valid."

                    500 -> "Terjadi kesalahan pada server. Silakan coba lagi nanti."
                    else -> errorResponse?.message ?: "Registrasi gagal. Silakan coba lagi."
                }

                emit(Resource.Error(errorMessage, response.code(), errorResponse))
            }
        } catch (e: Exception) {
            val errorMessage = if (e is IOException) {
                "Tidak dapat terhubung ke server. Periksa koneksi internet Anda."
            } else {
                "Terjadi kesalahan yang tidak terduga. Silakan coba lagi nanti."
            }
            emit(Resource.Error(errorMessage))
        }
    }.flowOn(Dispatchers.IO)
}