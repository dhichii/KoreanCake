package com.dliemstore.koreancake.data.source.repository.auth

import com.dliemstore.koreancake.data.api.service.AuthService
import com.dliemstore.koreancake.data.source.remote.request.auth.RegisterRequest
import com.dliemstore.koreancake.util.ApiUtils
import com.dliemstore.koreancake.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AuthRepository @Inject constructor(private val authService: AuthService) {
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
                val errorResponse = try {
                    ApiUtils.parseError(response)
                } catch (e: Exception) {
                    null
                }

                emit(Resource.Error(null, response.code(), errorResponse))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Something went wrong, Please try again later!"))
        }
    }.flowOn(Dispatchers.IO)
}