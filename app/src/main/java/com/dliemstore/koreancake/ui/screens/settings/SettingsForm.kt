package com.dliemstore.koreancake.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dliemstore.koreancake.ui.components.PrimaryButton
import com.dliemstore.koreancake.ui.components.TextInput
import com.dliemstore.koreancake.ui.navigation.graphs.SettingType

@Composable
fun SettingsForm(
    settingType: SettingType,
    navController: NavController,
) {
    var inputValue by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    fun validateAndSave() {
        if (inputValue.isBlank()) {
            errorMessage = "Required"
            return
        }

        if (settingType is SettingType.Email && !android.util.Patterns.EMAIL_ADDRESS.matcher(
                inputValue
            ).matches()
        ) {
            errorMessage = "Invalid email format"
            return
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        TextInput(
            value = inputValue,
            onInputChanged = {
                inputValue = it
                errorMessage = null
            },
            label = settingType.fieldLabel,
            errorMessage = errorMessage
        )

        Spacer(modifier = Modifier.height(16.dp))
        PrimaryButton(
            text = "Simpan",
            enabled = inputValue != "",
            onClick = {
                validateAndSave()
                if (errorMessage == null) {
                    navController.popBackStack()
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
