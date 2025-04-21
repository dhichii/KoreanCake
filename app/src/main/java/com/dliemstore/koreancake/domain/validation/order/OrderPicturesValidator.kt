package com.dliemstore.koreancake.domain.validation.order

import android.net.Uri
import com.dliemstore.koreancake.util.ValidationHelper

object OrderPicturesValidator {
    fun validate(pictures: List<Uri>): String? {
        return listOfNotNull(
            ValidationHelper.required(pictures, "Gambar"),
            ValidationHelper.maxInteger(pictures.size, 3, "Gambar")
        ).firstOrNull()
    }
}