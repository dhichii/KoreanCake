package com.dliemstore.koreancake.util

import com.dliemstore.koreancake.data.source.remote.response.ApiErrorResponse
import com.dliemstore.koreancake.data.source.remote.response.FieldError
import com.google.gson.Gson
import retrofit2.Response

object ApiUtils {
    fun parseError(response: Response<*>): ApiErrorResponse {
        return try {
            val errorBody = response.errorBody()?.string()
            Gson().fromJson(errorBody, ApiErrorResponse::class.java)
        } catch (e: Exception) {
            ApiErrorResponse("Parsing error")
        }
    }

    fun extractApiFieldErrors(apiFieldErrors: List<FieldError>?): Map<String, String> {
        return apiFieldErrors?.associate { it.path to it.message } ?: emptyMap()
    }
}