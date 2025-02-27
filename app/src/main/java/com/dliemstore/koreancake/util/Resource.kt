package com.dliemstore.koreancake.util

import com.dliemstore.koreancake.data.source.remote.response.FieldError

sealed class Resource<T>(
    val data: T? = null,
    val msg: String? = null,
    val code: Int? = null,
    val errors: List<FieldError>? = null
) {
    class Loading<T> : Resource<T>()
    class Success<T>(data: T?, code: Int?) : Resource<T>(data, code = code)
    class Error<T>(msg: String?, code: Int? = null, errors: List<FieldError>? = null) :
        Resource<T>(null, msg, code, errors)
}