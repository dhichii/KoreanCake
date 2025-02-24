package com.dliemstore.koreancake.data.source.remote.response.auth

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("access") val access: String,
)
