package com.dliemstore.koreancake.data.source.remote.response.order

data class PicturesItem(
    val id: String,
    val url: String
)

data class OrdersResponse(
    val id: String,
    val price: Double,
    val downPayment: Double,
    val remainingPayment: Double,
    val status: String,
    val pickupTime: Long,
    val layer: Int?,
    val size: Int,
    val text: String,
    val telp: String,
    val notes: String?,
    val isUseTopper: Boolean,
    val textColor: String,
    val pictures: List<PicturesItem>
)

