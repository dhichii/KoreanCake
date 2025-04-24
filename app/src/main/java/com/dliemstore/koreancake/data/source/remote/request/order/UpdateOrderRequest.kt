package com.dliemstore.koreancake.data.source.remote.request.order

data class UpdateOrderRequest(
    val size: Int,
    val layer: Int?,
    val isUseTopper: Boolean,
    val pickupTime: String,
    val text: String,
    val textColor: String,
    val notes: String?,
    val telp: String,
    val price: Double,
    val downPayment: Double,
    val deletedPictures: List<String>,
    val addedProgresses: List<String>,
    val deletedProgresses: List<String>
)
