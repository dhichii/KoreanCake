package com.dliemstore.koreancake.ui.screens.order

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dliemstore.koreancake.R
import com.dliemstore.koreancake.data.source.remote.response.process.ProcessResponse
import com.dliemstore.koreancake.ui.components.CurrencyInput
import com.dliemstore.koreancake.ui.components.CustomCheckBox
import com.dliemstore.koreancake.ui.components.CustomDatePicker
import com.dliemstore.koreancake.ui.components.ErrorText
import com.dliemstore.koreancake.ui.components.PickupTimeInput
import com.dliemstore.koreancake.ui.components.ProgressInput
import com.dliemstore.koreancake.ui.components.TelpInput
import com.dliemstore.koreancake.ui.components.TextInput
import com.dliemstore.koreancake.ui.components.pickupMinuteItems
import com.dliemstore.koreancake.ui.state.order.OrderFormState
import com.dliemstore.koreancake.util.Resource
import com.dliemstore.koreancake.util.hours

@Composable
fun OrderForm(
    orderFormState: OrderFormState,
    progressesState: Resource<List<ProcessResponse>>,
    navController: NavController,
    onInputChange: (String, Any) -> Unit,
    onProgressRetry: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 8.dp)
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
                value = orderFormState.size,
                onInputChanged = { onInputChange("size", it) },
                label = "Ukuran",
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next,
                modifier = Modifier.fillMaxWidth(0.47f),
                errorMessage = orderFormState.errors["size"]
            )
            TextInput(
                value = orderFormState.layer,
                onInputChanged = { onInputChange("layer", it) },
                label = "Layer (Opsional)",
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next,
                errorMessage = orderFormState.errors["layer"]
            )
        }
        TextInput(
            value = orderFormState.text,
            onInputChanged = { onInputChange("text", it) },
            label = "Tulisan",
            imeAction = ImeAction.Next,
            errorMessage = orderFormState.errors["text"]
        )
        TextInput(
            value = orderFormState.textColor,
            onInputChanged = { onInputChange("textColor", it) },
            label = "Warna Tulisan",
            errorMessage = orderFormState.errors["textColor"]
        )
        Column {
            CustomCheckBox(
                isChecked = orderFormState.isUseTopper,
                onClicked = { onInputChange("isUseTopper", it) },
                label = "Pakai Topper",
            )
            orderFormState.errors["isUseTopper"]?.let {
                ErrorText(text = it)
            }
        }
        ProgressInput(
            options = progressesState.data ?: emptyList(),
            selectedOptions = orderFormState.progresses,
            status = progressesState,
            errorMessage = orderFormState.errors["progresses"],
            navController = navController,
            onRetry = onProgressRetry,
            onSelectionChanged = { onInputChange("progresses", it) }
        )
        CustomDatePicker(
            value = orderFormState.pickupDate,
            onSelected = {
                if (it != null) {
                    onInputChange("pickupDate", it)
                }
            },
            label = "Tanggal Pengambilan",
            errorMessage = orderFormState.errors["pickupDate"]
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            PickupTimeInput(
                value = orderFormState.pickupHour,
                items = hours,
                onSelected = { onInputChange("pickupHour", it) },
                label = "Jam",
                modifier = Modifier.fillMaxWidth(0.47f),
                errorMessage = orderFormState.errors["pickupHour"]
            )
            PickupTimeInput(
                value = orderFormState.pickupMinute,
                items = pickupMinuteItems,
                onSelected = { onInputChange("pickupMinute", it) },
                label = "Menit",
                errorMessage = orderFormState.errors["pickupMinute"]
            )
        }
        TelpInput(
            value = orderFormState.telp,
            onInputChanged = { onInputChange("telp", it) },
            imeAction = ImeAction.Next,
            errorMessage = orderFormState.errors["telp"]
        )
        CurrencyInput(
            value = orderFormState.price,
            label = "Harga",
            imeAction = ImeAction.Next,
            errorMessage = orderFormState.errors["price"],
            onInputChanged = { onInputChange("price", it) }
        )
        CurrencyInput(
            value = orderFormState.downPayment,
            label = "Uang Muka",
            imeAction = ImeAction.Next,
            errorMessage = orderFormState.errors["downPayment"],
            onInputChanged = { onInputChange("downPayment", it) }
        )
        TextInput(
            value = orderFormState.notes,
            onInputChanged = { onInputChange("notes", it) },
            label = "Catatan (Opsional)",
            errorMessage = orderFormState.errors["notes"]
        )
    }
}