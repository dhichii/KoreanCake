package com.dliemstore.koreancake.data.source.repository.user

import com.dliemstore.koreancake.data.api.PersistentCookieStore
import com.dliemstore.koreancake.data.api.TokenManager
import com.dliemstore.koreancake.data.api.service.UserService
import com.dliemstore.koreancake.data.source.remote.request.user.UpdateEmailRequest
import com.dliemstore.koreancake.data.source.remote.request.user.UpdatePasswordRequest
import com.dliemstore.koreancake.data.source.remote.request.user.UpdateProfileRequest
import com.dliemstore.koreancake.data.source.remote.request.user.UpdateUsernameRequest
import com.dliemstore.koreancake.util.ApiUtils
import com.dliemstore.koreancake.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userService: UserService,
    private val tokenManager: TokenManager,
    private val persistentCookieStore: PersistentCookieStore
) {
    fun getProfile() = flow {
        emit(Resource.Loading())

        try {
            val response = userService.getProfile()
            if (response.isSuccessful) {
                emit(Resource.Success(response.body()?.data, response.code()))
            } else {
                val errorResponse = runCatching { ApiUtils.parseError(response) }.getOrNull()
                val errorMessage = when (response.code()) {
                    500 -> "Terjadi kesalahan pada server"
                    else -> errorResponse?.message ?: "Permintaan tidak valid."
                }

                emit(Resource.Error(errorMessage, response.code(), errorResponse?.errors))
            }
        } catch (e: Exception) {
            val errorMessage = if (e is IOException) {
                "Tidak ada koneksi internet."
            } else {
                "Terjadi kesalahan yang tidak terduga"
            }
            emit(Resource.Error(errorMessage))
        }
    }.flowOn(Dispatchers.IO)

    fun updateProfile(name: String): Flow<Resource<Unit>> = updateUser {
        userService.updateProfile(UpdateProfileRequest(name))
    }

    fun updateEmail(password: String, email: String): Flow<Resource<Unit>> =
        updateUser(shouldLogout = true) {
            userService.updateEmail(UpdateEmailRequest(password, email))
        }

    fun updateUsername(password: String, username: String): Flow<Resource<Unit>> =
        updateUser(shouldLogout = true) {
            userService.updateUsername(UpdateUsernameRequest(password, username))
        }

    fun updatePassword(oldPassword: String, newPassword: String): Flow<Resource<Unit>> =
        updateUser(shouldLogout = true) {
            userService.updatePassword(
                UpdatePasswordRequest(
                    oldPassword = oldPassword,
                    newPassword = newPassword
                )
            )
        }

    private fun updateUser(
        shouldLogout: Boolean = false,
        apiCall: suspend () -> Response<Unit>
    ): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())

        try {
            val response = apiCall()
            if (response.isSuccessful) {
                if (shouldLogout) {
                    tokenManager.clearToken()
                    persistentCookieStore.clear()
                }

                emit(Resource.Success(Unit, response.code()))
            } else {
                val errorResponse = runCatching { ApiUtils.parseError(response) }.getOrNull()
                val errorMessage = when (response.code()) {
                    500 -> "Terjadi kesalahan pada server"
                    else -> errorResponse?.message ?: "Update gagal. Silakan coba lagi."
                }

                emit(Resource.Error(errorMessage, response.code(), errorResponse?.errors))
            }
        } catch (e: Exception) {
            val errorMessage = if (e is IOException) {
                "Tidak ada koneksi internet."
            } else {
                "Terjadi kesalahan yang tidak terduga"
            }
            emit(Resource.Error(errorMessage))
        }
    }.flowOn(Dispatchers.IO)
}