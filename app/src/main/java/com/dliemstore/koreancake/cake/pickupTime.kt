package com.dliemstore.koreancake.cake

import com.dliemstore.koreancake.util.countRemainingDay
import com.dliemstore.koreancake.util.epochToDate
import com.dliemstore.koreancake.util.formatDate
import com.dliemstore.koreancake.util.formatTime

data class PickupTimePayload(
    val date: String,
    val time: String,
    val remainingDay: Int,
)

fun Long.transformPickupTime(): PickupTimePayload {
    val date = this.epochToDate()
    return PickupTimePayload(
        date = date.formatDate(),
        time = date.formatTime(),
        remainingDay = date.countRemainingDay()
    )
}
