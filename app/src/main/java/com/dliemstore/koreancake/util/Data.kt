package com.dliemstore.koreancake.util

import com.dliemstore.koreancake.R

data class CakeData(
    val id: String,
    val price: Double,
    val downPayment: Double,
    val remainingPayment: Double,
    val status: String,
    val pickupTime: Long,
    val picture: Int,
    val telp: String,
    val size: Int,
    val layer: Int? = null,
    val text: String,
    val notes: String? = null,
    val textColor: String,
    val isUseTopper: Boolean = false,
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
        price = 300000.0,
        downPayment = 100000.0,
        remainingPayment = 200000.0,
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
        price = 300000.0,
        downPayment = 100000.0,
        remainingPayment = 200001.0,
        status = "Proses",
        pickupTime = 1730952000000,
        picture = R.drawable.ic_launcher_background,
        telp = "6284348434398",
        notes = "Merah",
        size = 16,
        isUseTopper = true,
        layer = 3,
        text = "HBD",
        textColor = "Putih",
        progress = progressList
    ),
    CakeData(
        id = "3",
        price = 300000.0,
        downPayment = 100000.0,
        remainingPayment = 200002.0,
        status = "Proses",
        pickupTime = 1730952000000,
        picture = R.drawable.ic_launcher_background,
        telp = "6284348434398",
        notes = "Merah",
        size = 16,
        text = "HBD",
        textColor = "Putih",
        isUseTopper = true,
        progress = progressList
    ),
    CakeData(
        id = "4",
        price = 300000.0,
        downPayment = 100000.0,
        remainingPayment = 200003.0,
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
        price = 300000.0,
        downPayment = 100000.0,
        remainingPayment = 200004.0,
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
        price = 300000.0,
        downPayment = 100000.0,
        remainingPayment = 200005.0,
        status = "Proses",
        pickupTime = 1730952000000,
        picture = R.drawable.ic_launcher_background,
        telp = "6284348434398",
        notes = "Merah",
        size = 16,
        text = "HBD",
        textColor = "Putih",
        isUseTopper = true,
        progress = progressList
    ),
    CakeData(
        id = "7",
        price = 300000.0,
        downPayment = 100000.0,
        remainingPayment = 200006.0,
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
        price = 300000.0,
        downPayment = 100000.0,
        remainingPayment = 200007.0,
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
        price = 300000.0,
        downPayment = 100000.0,
        remainingPayment = 200008.0,
        status = "Selesai",
        pickupTime = 1730566800000,
        picture = R.drawable.ic_launcher_background,
        telp = "6284348434398",
        notes = "Merah",
        size = 16,
        text = "HBD",
        textColor = "Putih",
        isUseTopper = true,
        progress = progressList
    ),
    CakeData(
        id = "10",
        price = 300000.0,
        downPayment = 100000.0,
        remainingPayment = 200009.0,
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
