package com.dliemstore.koreancake.ui.screens.process

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dliemstore.koreancake.ui.components.PrimaryButton
import com.dliemstore.koreancake.ui.components.TextInput
import com.dliemstore.koreancake.util.getProcessById

@Composable
fun EditProcess(id: String, navController: NavController) {
    val process = getProcessById(id)

    var name by rememberSaveable { mutableStateOf(process.name) }
    var step by rememberSaveable { mutableStateOf(process.step.toString()) }

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 12.dp, end = 12.dp, bottom = 80.dp)
    ) {
        TextInput(value = name, onInputChanged = { name = it }, label = "Nama")
        TextInput(
            value = step,
            onInputChanged = { step = it },
            label = "Proses ke-"
        )

        // Save button
        Spacer(modifier = Modifier.height(16.dp))
        PrimaryButton(
            text = "Simpan",
            onClick = {
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}