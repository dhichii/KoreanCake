package com.dliemstore.koreancake.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dliemstore.koreancake.ui.components.PasswordInput
import com.dliemstore.koreancake.ui.components.PrimaryButton
import com.dliemstore.koreancake.ui.components.SecondaryButton
import com.dliemstore.koreancake.ui.components.TextInput
import com.dliemstore.koreancake.ui.navigation.graphs.AuthNavigationItem
import com.dliemstore.koreancake.ui.navigation.graphs.Graph

@Composable
fun Login(navController: NavController) {
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Surface {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp, 0.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Login", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                TextInput(
                    value = username,
                    onInputChanged = { username = it },
                    label = "Username"
                )
                PasswordInput(value = password, onInputChanged = { password = it })
                PrimaryButton(
                    text = "Login",
                    onClick = {
                        navController.navigate(Graph.MAIN) {
                            popUpTo(AuthNavigationItem.Login.route) { inclusive = true }
                        }
                    },
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
                text = "Buat akun baru",
                onClick = { navController.navigate(AuthNavigationItem.Register.route) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}