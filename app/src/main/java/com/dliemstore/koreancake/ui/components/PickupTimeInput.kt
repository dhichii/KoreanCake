package com.dliemstore.koreancake.ui.components

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

val pickupMinuteItems = listOf("00", "30")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickupTimeInput(
    value: String,
    items: List<String>,
    onSelected: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    var isShowed by remember { mutableStateOf(false) }
    var timeState by remember { mutableStateOf(value) }

    Box(modifier = modifier) {
        TextInput(
            value = timeState, onInputChanged = {}, label = label, trailingIcon = {
                Icon(
                    if (isShowed) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = label
                )
            },
            modifier = Modifier.pointerInput(isShowed) {
                awaitEachGesture {
                    // Modifier.clickable doesn't work for text fields, so we use Modifier.pointerInput
                    // in the Initial pass to observe events before the text field consumes them
                    // in the Main pass.
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        isShowed = true
                    }
                }
            }
        )
        if (isShowed) {
            ModalBottomSheet(
                onDismissRequest = { isShowed = false },
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .padding(12.dp, 8.dp)
                        .fillMaxHeight(0.4f)
                        .verticalScroll(rememberScrollState())
                ) {
                    items.forEach { item ->
                        TextButton(
                            onClick = {
                                onSelected(item)
                                timeState = item
                                isShowed = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(item)
                        }
                    }
                }
            }
        }
    }
}
