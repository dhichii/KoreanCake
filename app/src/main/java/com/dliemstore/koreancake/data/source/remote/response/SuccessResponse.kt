package com.dliemstore.koreancake.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class SuccessResponse<T>(
    @SerializedName("data") val data: T,
)