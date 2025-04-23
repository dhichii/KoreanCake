package com.dliemstore.koreancake.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.text.NumberFormat
import java.util.Locale

fun Double.formatCurrency(): String {
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
    currencyFormat.maximumFractionDigits = 0
    return currencyFormat.format(this)
}

class CurrencyVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val rawText = text.text
        val formatted = rawText.toDoubleOrNull()?.formatCurrency()?.removePrefix("Rp") ?: ""

        return TransformedText(AnnotatedString(formatted), object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val delta = formatted.length - rawText.length
                return (offset + delta).coerceIn(0, formatted.length)
            }

            override fun transformedToOriginal(offset: Int): Int {
                val delta = formatted.length - rawText.length
                return (offset - delta).coerceIn(0, rawText.length)
            }
        })
    }
}
