package com.dliemstore.koreancake.util

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId
import java.util.Date
import java.util.Locale


fun Long.epochToDate(): Date {
    return Date(this)
}

fun Date.formatDate(): String {
    val date = SimpleDateFormat("EEE, d MMMM yyyy", Locale.getDefault()).format(this)
    val dayName = date.split(",")[0]

    return date.replace(dayName, dayName.translateDayToIndonesia())
}

fun String.translateDayToIndonesia(): String {
    val days = mapOf(
        "Sun" to "Minggu",
        "Mon" to "Senin",
        "Tue" to "Selasa",
        "Wed" to "Rabu",
        "Thu" to "Kamis",
        "Fri" to "Jumat",
        "Sat" to "Sabtu"
    )

    return days.getValue(this)
}

fun Date.formatTime(): String {
    return SimpleDateFormat("HH:mm", Locale.getDefault()).format(this)
}

fun Date.countRemainingDay(): Int {
    val today = LocalDate.now()

//    convert input date to LocalDate
    val instant = this.toInstant()
    val zonedDateTime = instant.atZone(ZoneId.systemDefault())
    val to = zonedDateTime.toLocalDate()

    return Period.between(today, to).days
}

val hours = listOf(
    "00",
    "01",
    "02",
    "03",
    "04",
    "05",
    "06",
    "07",
    "08",
    "09",
    "10",
    "11",
    "12",
    "13",
    "14",
    "15",
    "16",
    "17",
    "18",
    "19",
    "20",
    "21",
    "22",
    "23"
)
