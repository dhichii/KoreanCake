package com.dliemstore.koreancake.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class SuccessResponse<T>(
    @SerializedName("data") val data: T,
)

data class PaginationSuccessResponse<T>(
    @SerializedName("limit") val limit: Int,
    @SerializedName("totalPage") val totalPage: Int,
    @SerializedName("totalResult") val totalResult: Int,
    @SerializedName("page") val page: Int,
    @SerializedName("data") val data: List<T>,
)