package com.dliemstore.koreancake.util

import com.dliemstore.koreancake.R

data class CakeData(
    val id: String,
    val price: String,
    val downPayment: String,
    val remainingPayment: String,
    val status: String,
    val pickupTime: Long,
    val picture: Int,
    val telp: String,
    val size: Int,
    val layer: Int? = null,
    val text: String,
    val notes: String? = null,
    val textColor: String,
    val progress: List<Progress>
)

data class Progress(
    val id: Int,
    val name: String,
    val isFinish: Boolean
)

val progressList = listOf(
    Progress(
        id = 1,
        name = "Krim",
        isFinish = true
    ),
    Progress(
        id = 2,
        name = "Kue",
        isFinish = true
    ),
    Progress(
        id = 3,
        name = "Tulis",
        isFinish = false
    ),
    Progress(
        id = 4,
        name = "Kotak",
        isFinish = true
    ),
    Progress(
        id = 5,
        name = "Topper",
        isFinish = true
    )
)

val cakeList = listOf(
    CakeData(
        id = "1",
        price = "Rp 300.000",
        downPayment = "Rp 100.000",
        remainingPayment = "Rp 200.000",
        status = "Proses",
        pickupTime = 1730952000000,
        picture = R.drawable.ic_launcher_background,
        telp = "6284348434398",
        notes = "Merah",
        size = 16,
        layer = 2,
        text = "HBD",
        textColor = "Putih",
        progress = progressList
    ),
    CakeData(
        id = "2",
        price = "Rp 300.000",
        downPayment = "Rp 100.000",
        remainingPayment = "Rp 200.001",
        status = "Proses",
        pickupTime = 1730952000000,
        picture = R.drawable.ic_launcher_background,
        telp = "6284348434398",
        notes = "Merah",
        size = 16,
        layer = 3,
        text = "HBD",
        textColor = "Putih",
        progress = progressList
    ),
    CakeData(
        id = "3",
        price = "Rp 300.000",
        downPayment = "Rp 100.000",
        remainingPayment = "Rp 200.002",
        status = "Proses",
        pickupTime = 1730952000000,
        picture = R.drawable.ic_launcher_background,
        telp = "6284348434398",
        notes = "Merah",
        size = 16,
        text = "HBD",
        textColor = "Putih",
        progress = progressList
    ),
    CakeData(
        id = "4",
        price = "Rp 300.000",
        downPayment = "Rp 100.000",
        remainingPayment = "Rp 200.003",
        status = "Proses",
        pickupTime = 1730952000000,
        picture = R.drawable.ic_launcher_background,
        telp = "6284348434398",
        notes = "Merah",
        size = 16,
        text = "HBD",
        textColor = "Putih",
        progress = progressList
    ),
    CakeData(
        id = "5",
        price = "Rp 300.000",
        downPayment = "Rp 100.000",
        remainingPayment = "Rp 200.004",
        status = "Proses",
        pickupTime = 1730566800000,
        picture = R.drawable.ic_launcher_background,
        telp = "6284348434398",
        notes = "Merah",
        size = 16,
        layer = 2,
        text = "HBD",
        textColor = "Putih",
        progress = progressList
    ),
    CakeData(
        id = "6",
        price = "Rp 300.000",
        downPayment = "Rp 100.000",
        remainingPayment = "Rp 200.005",
        status = "Proses",
        pickupTime = 1730952000000,
        picture = R.drawable.ic_launcher_background,
        telp = "6284348434398",
        notes = "Merah",
        size = 16,
        text = "HBD",
        textColor = "Putih",
        progress = progressList
    ),
    CakeData(
        id = "7",
        price = "Rp 300.000",
        downPayment = "Rp 100.000",
        remainingPayment = "Rp 200.006",
        status = "Proses",
        pickupTime = 1730952000000,
        picture = R.drawable.ic_launcher_background,
        telp = "6284348434398",
        notes = "Merah",
        size = 16,
        text = "HBD",
        textColor = "Putih",
        progress = progressList
    ),
    CakeData(
        id = "8",
        price = "Rp 300.000",
        downPayment = "Rp 100.000",
        remainingPayment = "Rp 200.007",
        status = "Proses",
        pickupTime = 1730952000000,
        picture = R.drawable.ic_launcher_background,
        telp = "6284348434398",
        size = 16,
        text = "HBD",
        textColor = "Putih",
        progress = progressList
    ),
    CakeData(
        id = "9",
        price = "Rp 300.000",
        downPayment = "Rp 100.000",
        remainingPayment = "Rp 200.008",
        status = "Selesai",
        pickupTime = 1730566800000,
        picture = R.drawable.ic_launcher_background,
        telp = "6284348434398",
        notes = "Merah",
        size = 16,
        text = "HBD",
        textColor = "Putih",
        progress = progressList
    ),
    CakeData(
        id = "10",
        price = "Rp 300.000",
        downPayment = "Rp 100.000",
        remainingPayment = "Rp 200.009",
        status = "Selesai",
        pickupTime = 1730566800000,
        picture = R.drawable.ic_launcher_background,
        telp = "6284348434398",
        size = 16,
        layer = 2,
        text = "HBD",
        textColor = "Putih",
        progress = progressList
    )
)

fun getAllCakeData(status: String): List<CakeData> {
    if (status == "All") {
        return cakeList
    }

    return cakeList.filter { value -> value.status == status }
}

fun getCakeData(id: String): CakeData {
    return cakeList.find { value -> value.id == id }!!
}
