package com.dliemstore.koreancake.ui.screens.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dliemstore.koreancake.ui.components.PasswordInput
import com.dliemstore.koreancake.ui.components.TextInput
import com.dliemstore.koreancake.ui.navigation.graphs.AuthNavigationItem

@Composable
fun Register(navController: NavController) {
    var name by rememberSaveable { mutableStateOf("") }
    var username by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }

    val hasMinimum: Boolean = password.matches(Regex(".{8,}"))
    val isMatched: Boolean = confirmPassword == password
    var isConfirmPasswordChanged by rememberSaveable { mutableStateOf(false) }

    Scaffold { contentPadding ->
        Surface(modifier = Modifier.padding(contentPadding)) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp, 0.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "Buat Akun", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    TextInput(
                        value = name,
                        onInputChanged = { name = it },
                        label = "Nama"
                    )
                    TextInput(
                        value = username,
                        onInputChanged = { username = it },
                        label = "Username"
                    )
                    TextInput(
                        value = email,
                        onInputChanged = { email = it },
                        label = "Email",
                        keyboardType = KeyboardType.Email
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        PasswordInput(value = password, onInputChanged = { password = it })
                        Row {
                            Icon(
                                imageVector = if (hasMinimum) Icons.Filled.Check else Icons.Filled.Close,
                                tint = if (hasMinimum) Color.Green else MaterialTheme.colorScheme.error,
                                contentDescription = "has minimum"
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Minimal 8 huruf",
                                color = MaterialTheme.colorScheme.inverseSurface
                            )
                        }
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        PasswordInput(
                            value = confirmPassword,
                            label = "Konfirmasi Password",
                            onInputChanged = {
                                confirmPassword = it
                                if (!isConfirmPasswordChanged) isConfirmPasswordChanged = true
                            },
                            isError = !isMatched && isConfirmPasswordChanged
                        )
                        if (!isMatched && isConfirmPasswordChanged) Text("Konfirmasi password tidak sama")
                    }
                    Button(
                        onClick = { navController.navigate(AuthNavigationItem.Login.route) },
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Daftar")
                    }
                }
            }
            Box(
                contentAlignment = Alignment.BottomCenter,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp, 8.dp)
            ) {
                OutlinedButton(
                    onClick = { navController.navigate(AuthNavigationItem.Login.route) },
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Sudah punya akun")
                }
            }
        }
    }
}