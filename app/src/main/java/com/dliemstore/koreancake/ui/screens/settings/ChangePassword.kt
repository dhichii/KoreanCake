package com.dliemstore.koreancake.ui.screens.settings

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import com.dliemstore.koreancake.ui.components.PasswordInput
import com.dliemstore.koreancake.ui.components.PrimaryButton
import com.dliemstore.koreancake.ui.navigation.graphs.ScaffoldViewState
import com.dliemstore.koreancake.ui.navigation.graphs.TopAppBarItem
import com.dliemstore.koreancake.ui.navigation.graphs.TopAppBarNavigationIcon
import com.dliemstore.koreancake.ui.viewmodel.settings.ChangePasswordViewModel

@Composable
fun ChangePassword(
    navController: NavController,
    scaffoldViewState: MutableState<ScaffoldViewState>,
    viewModel: ChangePasswordViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val changePasswordState by viewModel.changePasswordState.collectAsState()
    val isPrimaryButtonEnabled =
        changePasswordState.oldPassword.isNotBlank() &&
                changePasswordState.newPassword.isNotBlank() &&
                changePasswordState.confirmPassword.isNotBlank() &&
                changePasswordState.errors.isEmpty()

    LaunchedEffect(Unit) {
        scaffoldViewState.value = ScaffoldViewState(
            TopAppBarItem(
                title = { Text("Password") },
                navigationIcon = TopAppBarNavigationIcon.CLOSE
            )
        )
    }

    LaunchedEffect(changePasswordState) {
        when {
            changePasswordState.isSuccess -> {
                Toast.makeText(context, "Password berhasil diubah!", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            }

            changePasswordState.errorMessage != null -> {
                Toast.makeText(context, changePasswordState.errorMessage, Toast.LENGTH_SHORT).show()
                viewModel.clearErrorMessage()
            }
        }
    }

    Column(modifier = Modifier.padding(horizontal = 12.dp)) {
        PasswordInput(
            value = changePasswordState.oldPassword,
            label = "Password Sekarang",
            onInputChanged = { viewModel.onInputChange("oldPassword", it) },
            errorMessage = changePasswordState.errors["oldPassword"]
        )
        PasswordInput(
            value = changePasswordState.newPassword,
            label = "Password Baru",
            onInputChanged = { viewModel.onInputChange("newPassword", it) },
            errorMessage = changePasswordState.errors["newPassword"]
        )
        PasswordInput(
            value = changePasswordState.confirmPassword,
            label = "Konfirmasi Password",
            onInputChanged = {
                viewModel.onInputChange("confirmPassword", it)
            },
            errorMessage = changePasswordState.errors["confirmPassword"]
        )

        Spacer(modifier = Modifier.height(16.dp))
        PrimaryButton(
            text = "Simpan",
            enabled = isPrimaryButtonEnabled,
            isLoading = changePasswordState.isLoading,
            onClick = { viewModel.changePassword() },
            modifier = Modifier.fillMaxWidth()
        )
    }
}