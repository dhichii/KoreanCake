package com.dliemstore.koreancake.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ApiErrorResponse(
    @SerializedName("message") val message: String,
    @SerializedName("errors") val errors: List<FieldError>? = null
)

data class FieldError(
    @SerializedName("code") val code: String? = null,
    @SerializedName("expected") val expected: String? = null,
    @SerializedName("received") val received: String? = null,
    @SerializedName("path") val path: String,
    @SerializedName("message") val message: String
)