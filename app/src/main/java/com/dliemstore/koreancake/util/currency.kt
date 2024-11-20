package com.dliemstore.koreancake.util

import java.text.NumberFormat
import java.util.Locale

fun Double.formatCurrency(): String {
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("in", "ID"))

    return currencyFormat.format(this)
}
