package com.dliemstore.koreancake.ui.components

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dliemstore.koreancake.R
import com.dliemstore.koreancake.data.source.remote.response.process.ProcessResponse
import com.dliemstore.koreancake.ui.navigation.graphs.Graph
import com.dliemstore.koreancake.util.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressInput(
    options: List<ProcessResponse>,
    selectedOptions: List<String>,
    status: Resource<List<ProcessResponse>>,
    errorMessage: String? = null,
    modifier: Modifier = Modifier.fillMaxWidth(),
    navController: NavController,
    onRetry: () -> Unit,
    onSelectionChanged: (List<String>) -> Unit
) {
    var isShowed by remember { mutableStateOf(false) }
    val displayText =
        if (selectedOptions.isEmpty()) "" else options.filter { it.id in selectedOptions }
            .joinToString(", ") { it.name }

    TextInput(
        value = displayText,
        onInputChanged = {},
        label = "Progress",
        trailingIcon = {
            Icon(
                if (isShowed) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                contentDescription = "dropdown"
            )
        },
        readOnly = true,
        errorMessage = errorMessage,
        modifier = modifier.pointerInput(isShowed) {
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
            when (status) {
                is Resource.Loading -> Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.4f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }

                is Resource.Success ->
                    if (options.isNotEmpty()) ProgressInputContent(
                        options,
                        selectedOptions,
                        onSelectionChanged
                    )
                    else EmptyState {
                        isShowed = false
                        navController.navigate(Graph.PROCESS)
                    }

                is Resource.Error -> ErrorState { onRetry() }
            }
        }
    }
}

@Composable
fun ProgressInputContent(
    options: List<ProcessResponse>,
    selectedOptions: List<String>,
    onSelectionChanged: (List<String>) -> Unit
) {
    ProgressInputContentHeader(options, selectedOptions, onSelectionChanged)
    ProgressInputContentOptions(options, selectedOptions, onSelectionChanged)
}

@Composable
fun ProgressInputContentHeader(
    options: List<ProcessResponse>,
    selectedOptions: List<String>,
    onSelectionChanged: (List<String>) -> Unit
) {
    val isAllSelected =
        options.isNotEmpty() && selectedOptions.containsAll(options.map { it.id })
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                // draw shadow
                val shadowPx = 2.dp.toPx()
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Black.copy(alpha = 0.2f), Color.Transparent),
                        startY = size.height - shadowPx,
                        endY = size.height
                    ),
                    topLeft = Offset(0f, size.height - shadowPx),
                    size = Size(size.width, shadowPx)
                )
            }
    ) {
        CustomCheckBox(
            isChecked = isAllSelected,
            label = "Semua",
            modifier = Modifier.padding(12.dp, 8.dp)
        ) {
            if (isAllSelected) onSelectionChanged(emptyList())
            else onSelectionChanged(options.map { it.id })
        }
    }
}

@Composable
fun ProgressInputContentOptions(
    options: List<ProcessResponse>,
    selectedOptions: List<String>,
    onSelectionChanged: (List<String>) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.fillMaxHeight(0.4f)
    ) {
        items(options) { option ->
            CustomCheckBox(
                isChecked = selectedOptions.contains(option.id),
                label = option.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            ) {
                val newSelection =
                    if (selectedOptions.contains(option.id)) selectedOptions - option.id
                    else selectedOptions + option.id
                onSelectionChanged(newSelection)
            }
        }
    }
}

@Composable
private fun EmptyState(onCLick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.4f)
    ) {
        Icon(
            painter = painterResource(R.drawable.assignment_add_rounded_24),
            contentDescription = "Empty State",
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(100.dp)
        )
        Text(text = "Belum Ada Proses", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Coba tambahkan beberapa proses!", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        PrimaryButton("Tambah", onClick = onCLick)
    }
}

@Composable
private fun ErrorState(onRetry: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.4f),
        contentAlignment = Alignment.Center
    ) {
        RefreshButton { onRetry() }
    }
}

@Composable
private fun RefreshButton(onCLick: () -> Unit) {
    OutlinedButton(
        onClick = onCLick,
        shape = CircleShape,
        contentPadding = PaddingValues(0.dp)
    ) {
        Icon(
            imageVector = Icons.Rounded.Refresh,
            contentDescription = "Retry",
            modifier = Modifier
                .size(80.dp)
                .padding(12.dp)
        )
    }
}
