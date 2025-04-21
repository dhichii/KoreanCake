package com.dliemstore.koreancake.ui.state.order

import android.net.Uri

data class OrderPicturesState(
    val pictures: List<Uri> = emptyList(),
    val error: String? = null,
)
