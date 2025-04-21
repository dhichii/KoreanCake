package com.dliemstore.koreancake.domain.validation.order

import com.dliemstore.koreancake.ui.components.pickupMinuteItems
import com.dliemstore.koreancake.util.RegexUtils
import com.dliemstore.koreancake.util.ValidationHelper
import com.dliemstore.koreancake.util.hours

object OrderFormValidator {
    fun validate(
        size: String,
        layer: String,
        text: String,
        textColor: String,
        pickupDate: Long?,
        pickupHour: String,
        pickupMinute: String,
        telp: String,
        price: String,
        downPayment: String,
        progresses: List<String>,
    ): Map<String, String?> {
        return mapOf(
            "size" to listOfNotNull(
                ValidationHelper.required(size, "Ukuran"),
                ValidationHelper.isValidInteger(size, "Ukuran"),
                ValidationHelper.minInteger(size, 10, "Ukuran"),
            ).firstOrNull(),
            "layer" to if (layer.isNotBlank()) listOfNotNull(
                ValidationHelper.isValidInteger(layer, "Layer"),
                ValidationHelper.minInteger(layer, 1, "Layer")
            ).firstOrNull() else null,
            "text" to listOfNotNull(
                ValidationHelper.required(text, "Tulisan"),
                ValidationHelper.maxLength(text, 255, "Tulisan")
            ).firstOrNull(),
            "textColor" to listOfNotNull(
                ValidationHelper.required(textColor, "Warna tulisan"),
                ValidationHelper.maxLength(text, 120, "Warna tulisan")
            ).firstOrNull(),
            "pickupDate" to ValidationHelper.required(pickupDate, "Tanggal pengambilan"),
            "pickupHour" to if (pickupHour !in hours) "Jam tidak boleh kosong." else null,
            "pickupMinute" to if (pickupMinute !in pickupMinuteItems) "Menit tidak boleh kosong." else null,
            "telp" to listOfNotNull(
                ValidationHelper.required(telp, "Nomor telpon"),
                if (!RegexUtils.isValidPhoneNumber(telp)) "Nomor telpon tidak valid." else null,
            ).firstOrNull(),
            "price" to listOfNotNull(
                ValidationHelper.required(price, "Harga"),
                ValidationHelper.isValidDouble(price, "Harga"),
                ValidationHelper.minDouble(price, 1, "Harga"),
            ).firstOrNull(),
            "downPayment" to listOfNotNull(
                ValidationHelper.required(downPayment, "Uang muka"),
                ValidationHelper.isValidDouble(downPayment, "Uang muka"),
                ValidationHelper.minDouble(downPayment, 0, "Uang muka"),
            ).firstOrNull(),
            "progresses" to ValidationHelper.required(progresses, "Progres")
        ).filterValues { it != null }
    }
}