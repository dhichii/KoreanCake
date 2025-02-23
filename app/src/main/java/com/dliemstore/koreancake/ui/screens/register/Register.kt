package com.dliemstore.koreancake.ui.screens.register

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dliemstore.koreancake.ui.components.PasswordInput
import com.dliemstore.koreancake.ui.components.PrimaryButton
import com.dliemstore.koreancake.ui.components.SecondaryButton
import com.dliemstore.koreancake.ui.components.TextInput
import com.dliemstore.koreancake.ui.navigation.graphs.AuthNavigationItem
import com.dliemstore.koreancake.ui.viewmodel.auth.RegisterViewModel

@Composable
fun Register(navController: NavController, viewModel: RegisterViewModel = hiltViewModel()) {
    val registerState by viewModel.registerState.collectAsState()
    val context = LocalContext.current
    val isPrimaryButtonEnabled =
        registerState.name.isNotBlank() && registerState.email.isNotBlank() &&
                registerState.username.isNotBlank() && registerState.password.isNotBlank() &&
                registerState.confirmPassword.isNotBlank()

    LaunchedEffect(registerState.isSuccess) {
        if (registerState.isSuccess) {
            Toast.makeText(context, "Registrasi berhasil!", Toast.LENGTH_SHORT).show()
            navController.navigate(AuthNavigationItem.Login.route)
        }
    }

    LaunchedEffect(registerState.errorMessage) {
        registerState.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearErrorMessage()
        }
    }

    Surface {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp, 0.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Buat Akun", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                TextInput(
                    value = registerState.name,
                    onInputChanged = { viewModel.onInputChange("name", it) },
                    label = "Nama",
                    errorMessage = registerState.errors["name"]
                )
                TextInput(
                    value = registerState.username,
                    onInputChanged = { viewModel.onInputChange("username", it) },
                    label = "Username",
                    errorMessage = registerState.errors["username"]
                )
                TextInput(
                    value = registerState.email,
                    onInputChanged = { viewModel.onInputChange("email", it) },
                    label = "Email",
                    keyboardType = KeyboardType.Email,
                    errorMessage = registerState.errors["email"]
                )
                PasswordInput(
                    value = registerState.password,
                    onInputChanged = { viewModel.onInputChange("password", it) },
                    errorMessage = registerState.errors["password"]
                )
                PasswordInput(
                    value = registerState.confirmPassword,
                    label = "Konfirmasi Password",
                    onInputChanged = {
                        viewModel.onInputChange("confirmPassword", it)
                    },
                    errorMessage = registerState.errors["confirmPassword"]
                )

                PrimaryButton(
                    text = "Daftar",
                    isLoading = registerState.isLoading,
                    enabled = isPrimaryButtonEnabled,
                    onClick = { viewModel.register() },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp, 8.dp)
        ) {
            SecondaryButton(
                text = "Sudah punya akun",
                onClick = { navController.navigate(AuthNavigationItem.Login.route) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}