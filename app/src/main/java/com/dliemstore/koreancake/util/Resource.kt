package com.dliemstore.koreancake.util

import com.dliemstore.koreancake.data.source.remote.response.ApiErrorResponse

sealed class Resource<T>(
    val data: T? = null,
    val msg: String? = null,
    val code: Int? = null,
    val errorResponse: ApiErrorResponse? = null
) {
    class Loading<T> : Resource<T>()
    class Success<T>(data: T?, code: Int?) : Resource<T>(data, code = code)
    class Error<T>(msg: String?, code: Int? = null, errorResponse: ApiErrorResponse? = null) :
        Resource<T>(null, msg, code, errorResponse)
}