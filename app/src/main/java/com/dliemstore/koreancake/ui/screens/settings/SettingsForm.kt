package com.dliemstore.koreancake.ui.screens.settings

import android.widget.Toast
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dliemstore.koreancake.ui.components.PrimaryButton
import com.dliemstore.koreancake.ui.components.TextInput
import com.dliemstore.koreancake.ui.navigation.graphs.ScaffoldViewState
import com.dliemstore.koreancake.ui.navigation.graphs.SettingType
import com.dliemstore.koreancake.ui.navigation.graphs.TopAppBarItem
import com.dliemstore.koreancake.ui.navigation.graphs.TopAppBarNavigationIcon
import com.dliemstore.koreancake.ui.viewmodel.settings.SettingsFormViewModel

@Composable
fun SettingsForm(
    settingType: SettingType,
    navController: NavController,
    scaffoldViewState: MutableState<ScaffoldViewState>,
    topAppBarTitle: String,
    viewModel: SettingsFormViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val settingsFormState by viewModel.settingsFormState.collectAsState()
    val isPrimaryButtonEnabled =
        settingsFormState.input.isNotBlank() && settingsFormState.error == null

    LaunchedEffect(Unit) {
        scaffoldViewState.value = ScaffoldViewState(
            TopAppBarItem(
                title = { Text(topAppBarTitle) },
                navigationIcon = TopAppBarNavigationIcon.CLOSE
            )
        )
    }

    LaunchedEffect(settingsFormState) {
        when {
            settingsFormState.isSuccess -> {
                Toast.makeText(context, "update berhasil!", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            }

            settingsFormState.errorMessage != null -> {
                Toast.makeText(context, settingsFormState.errorMessage, Toast.LENGTH_SHORT).show()
                viewModel.clearErrorMessage()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        TextInput(
            value = settingsFormState.input,
            onInputChanged = { viewModel.onInputChange(settingType, it) },
            label = settingType.fieldLabel,
            errorMessage = settingsFormState.error
        )

        Spacer(modifier = Modifier.height(16.dp))
        PrimaryButton(
            text = "Simpan",
            enabled = isPrimaryButtonEnabled,
            isLoading = settingsFormState.isLoading,
            onClick = { viewModel.updateSetting(settingType) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
