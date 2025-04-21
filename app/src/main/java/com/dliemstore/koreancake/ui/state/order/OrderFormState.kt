package com.dliemstore.koreancake.ui.state.order

data class OrderFormState(
    val size: String = "",
    val layer: String = "",
    val text: String = "",
    val textColor: String = "",
    val isUseTopper: Boolean = false,
    val pickupDate: Long? = null,
    val pickupHour: String = "",
    val pickupMinute: String = "",
    val telp: String = "",
    val price: String = "",
    val downPayment: String = "",
    val notes: String = "",
    val progresses: List<String> = emptyList(),
    val errors: Map<String, String?> = emptyMap(),
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val statusCode: Int? = null,
    val errorMessage: String? = null
)
