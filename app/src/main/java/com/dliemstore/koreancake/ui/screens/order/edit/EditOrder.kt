package com.dliemstore.koreancake.ui.screens.order.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dliemstore.koreancake.R
import com.dliemstore.koreancake.ui.components.CustomCheckBox
import com.dliemstore.koreancake.ui.components.CustomDatePicker
import com.dliemstore.koreancake.ui.components.PickupTimeInput
import com.dliemstore.koreancake.ui.components.SecondaryButton
import com.dliemstore.koreancake.ui.components.TelpInput
import com.dliemstore.koreancake.ui.components.TextInput
import com.dliemstore.koreancake.ui.components.pickupMinuteItems
import com.dliemstore.koreancake.util.epochToDate
import com.dliemstore.koreancake.util.formatTime
import com.dliemstore.koreancake.util.getOrderData
import com.dliemstore.koreancake.util.hours

@Composable
fun EditOrder(id: String) {
    val order = getOrderData(id)
    val pickupTime = order.pickupTime.epochToDate().formatTime().split(":")
    val hour = pickupTime[0]
    val minute = pickupTime[1]

    var size by rememberSaveable { mutableStateOf(order.size.toString()) }
    var layer by rememberSaveable { mutableStateOf(order.layer?.toString() ?: "") }
    var text by rememberSaveable { mutableStateOf(order.text) }
    var textColor by rememberSaveable { mutableStateOf(order.textColor) }
    var useTopper by rememberSaveable { mutableStateOf(order.isUseTopper) }
    var pickupDate by rememberSaveable { mutableStateOf<Long?>(order.pickupTime) }
    var pickupHour by rememberSaveable { mutableStateOf(hour) }
    var pickupMinute by rememberSaveable { mutableStateOf(minute) }
    var telp by rememberSaveable { mutableStateOf(order.telp) }
    var price by rememberSaveable { mutableStateOf(order.price.toString()) }
    var downPayment by rememberSaveable { mutableStateOf(order.downPayment.toString()) }
    var remainingPayment by rememberSaveable { mutableStateOf(order.remainingPayment.toString()) }
    var notes by rememberSaveable { mutableStateOf(order.notes ?: "") }

    Surface {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp, 0.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(
                    text = "Gambar",
                    color = colorResource(R.color.black_700),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )
                SecondaryButton(text = "Tambah Foto", onClick = {})
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Text(
                    text = "Informasi Detail",
                    color = colorResource(R.color.black_700),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextInput(
                        value = size,
                        onInputChanged = { size = it },
                        label = "Ukuran",
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.fillMaxWidth(0.47f)
                    )
                    TextInput(
                        value = layer,
                        onInputChanged = { layer = it },
                        label = "Layer",
                        keyboardType = KeyboardType.Number
                    )
                }
                TextInput(value = text, onInputChanged = { text = it }, label = "Tulisan")
                TextInput(
                    value = textColor,
                    onInputChanged = { textColor = it },
                    label = "Warna Tulisan"
                )
                CustomCheckBox(
                    isChecked = useTopper,
                    onClicked = { useTopper = it },
                    label = "Pakai Topper"
                )
                CustomDatePicker(
                    value = pickupDate,
                    onSelected = { pickupDate = it },
                    label = "Tanggal Pengambilan"
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    PickupTimeInput(
                        value = pickupHour,
                        items = hours,
                        onSelected = { pickupHour = it },
                        label = "Jam",
                        modifier = Modifier.fillMaxWidth(0.47f),
                    )
                    PickupTimeInput(
                        value = pickupMinute,
                        items = pickupMinuteItems,
                        onSelected = { pickupMinute = it },
                        label = "Menit",
                    )
                }
                TelpInput(value = telp, onInputChanged = { telp = it })
                TextInput(
                    value = price,
                    onInputChanged = { price = it },
                    label = "Harga",
                    keyboardType = KeyboardType.Number
                )
                TextInput(
                    value = downPayment,
                    onInputChanged = { downPayment = it },
                    label = "Uang Muka",
                    keyboardType = KeyboardType.Number
                )
                TextInput(
                    value = remainingPayment,
                    onInputChanged = { remainingPayment = it },
                    label = "Sisa",
                    keyboardType = KeyboardType.Number
                )
                TextInput(value = notes, onInputChanged = { notes = it }, label = "Catatan")
            }
        }
    }
}
