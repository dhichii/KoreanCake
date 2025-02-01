package com.dliemstore.koreancake.ui.screens.add

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.dliemstore.koreancake.ui.components.TelpInput
import com.dliemstore.koreancake.ui.components.TextInput
import com.dliemstore.koreancake.ui.components.pickupMinuteItems
import com.dliemstore.koreancake.util.hours

@Composable
fun Add() {
    var size by rememberSaveable { mutableStateOf("") }
    var layer by rememberSaveable { mutableStateOf("") }
    var text by rememberSaveable { mutableStateOf("") }
    var textColor by rememberSaveable { mutableStateOf("") }
    var useTopper by rememberSaveable { mutableStateOf(false) }
    var pickupDate by rememberSaveable { mutableStateOf<Long?>(null) }
    var pickupHour by rememberSaveable { mutableStateOf("") }
    var pickupMinute by rememberSaveable { mutableStateOf("") }
    var telp by rememberSaveable { mutableStateOf("") }
    var price by rememberSaveable { mutableStateOf("") }
    var downPayment by rememberSaveable { mutableStateOf("") }
    var remainingPayment by rememberSaveable { mutableStateOf("") }
    var notes by rememberSaveable { mutableStateOf("") }

    Surface {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 12.dp, end = 12.dp, bottom = 80.dp)
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
                OutlinedButton(onClick = {}) { Text("Tambah Foto") }
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
                    value = useTopper,
                    onClicked = { useTopper = it },
                    label = "Pakai Topper"
                )
                CustomDatePicker(
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
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .padding(12.dp)
            ) {
                Button(
                    onClick = { },
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Simpan")
                }
            }
        }
    }
}
