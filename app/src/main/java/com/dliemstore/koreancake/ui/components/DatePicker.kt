package com.dliemstore.koreancake.ui.components

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.dliemstore.koreancake.util.epochToDate
import com.dliemstore.koreancake.util.formatDate

@Composable
fun CustomDatePicker(
    value: Long? = null,
    onSelected: (Long?) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    var selectedDate by remember { mutableStateOf(value) }
    var showModal by remember { mutableStateOf(false) }
    var labelFontSize by remember {
        mutableStateOf(14.sp)
    }

    OutlinedTextField(
        value = selectedDate?.epochToDate()?.formatDate() ?: "",
        onValueChange = {},
        label = { Text(label, fontSize = labelFontSize) },
        placeholder = { Text("MM/DD/YYYY", fontSize = 14.sp) },
        trailingIcon = {
            Icon(Icons.Rounded.DateRange, contentDescription = "Select date")
        },
        readOnly = true,
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(selectedDate) {
                awaitEachGesture {
                    // Modifier.clickable doesn't work for text fields, so we use Modifier.pointerInput
                    // in the Initial pass to observe events before the text field consumes them
                    // in the Main pass.
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showModal = true
                    }
                }
            }
            .onFocusChanged {
                labelFontSize =
                    if (it.isFocused || selectedDate != null) TextUnit.Unspecified else 14.sp
            }
    )

    if (showModal) {
        DatePickerModal(
            onDateSelected = {
                selectedDate = it
                onSelected(it)
            },
            onDismiss = { showModal = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}
