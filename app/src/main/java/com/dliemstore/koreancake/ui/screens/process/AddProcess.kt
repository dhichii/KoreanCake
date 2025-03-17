package com.dliemstore.koreancake.ui.screens.process

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dliemstore.koreancake.ui.components.PrimaryButton
import com.dliemstore.koreancake.ui.components.TextInput
import com.dliemstore.koreancake.ui.navigation.graphs.ScaffoldViewState
import com.dliemstore.koreancake.ui.navigation.graphs.TopAppBarItem
import com.dliemstore.koreancake.ui.navigation.graphs.TopAppBarNavigationIcon
import com.dliemstore.koreancake.ui.viewmodel.process.AddProcessViewModel

@Composable
fun AddProcess(
    navController: NavController,
    scaffoldViewState: MutableState<ScaffoldViewState>,
    viewModel: AddProcessViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val addProcessState by viewModel.addProcessState.collectAsState()

    val isPrimaryButtonEnabled =
        addProcessState.name.isNotBlank() && addProcessState.step.isNotBlank()
    val isAdding = addProcessState.isLoading

    LaunchedEffect(Unit) {
        scaffoldViewState.value = ScaffoldViewState(
            topAppBar = TopAppBarItem(
                title = { Text("Tambah Proses") },
                navigationIcon = TopAppBarNavigationIcon.CLOSE
            )
        )
    }

    LaunchedEffect(addProcessState) {
        when {
            addProcessState.isSuccess -> {
                Toast.makeText(context, "Proses berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            }

            addProcessState.errorMessage != null -> {
                Toast.makeText(context, addProcessState.errorMessage, Toast.LENGTH_SHORT).show()
                viewModel.clearErrorMessage()
            }
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 12.dp, end = 12.dp)
    ) {
        TextInput(
            value = addProcessState.name,
            onInputChanged = { viewModel.onInputChange("name", it) },
            label = "Nama",
            errorMessage = addProcessState.errors["name"]
        )
        TextInput(
            value = addProcessState.step,
            onInputChanged = { viewModel.onInputChange("step", it) },
            label = "Proses ke-",
            keyboardType = KeyboardType.Number,
            errorMessage = addProcessState.errors["step"]
        )

        // Save button
        Spacer(modifier = Modifier.height(16.dp))
        PrimaryButton(
            text = "Simpan",
            onClick = {
                keyboardController?.hide()
                viewModel.addProcess()
            },
            isLoading = isAdding,
            enabled = isPrimaryButtonEnabled,
            modifier = Modifier.fillMaxWidth()
        )
    }
}