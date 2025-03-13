package com.dliemstore.koreancake.ui.screens.login

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dliemstore.koreancake.ui.components.PasswordInput
import com.dliemstore.koreancake.ui.components.PrimaryButton
import com.dliemstore.koreancake.ui.components.SecondaryButton
import com.dliemstore.koreancake.ui.components.TextInput
import com.dliemstore.koreancake.ui.navigation.graphs.AuthNavigationItem
import com.dliemstore.koreancake.ui.navigation.graphs.Graph
import com.dliemstore.koreancake.ui.navigation.graphs.ScaffoldViewState
import com.dliemstore.koreancake.ui.viewmodel.auth.LoginViewModel

@Composable
fun Login(
    navController: NavController,
    scaffoldViewState: MutableState<ScaffoldViewState>,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val loginState by viewModel.loginState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) { scaffoldViewState.value = ScaffoldViewState() }

    LaunchedEffect(loginState.isSuccess) {
        if (loginState.isSuccess) {
            Toast.makeText(context, "Login berhasil!", Toast.LENGTH_SHORT).show()
            navController.navigate(Graph.MAIN) { popUpTo(Graph.AUTH) { inclusive = true } }
        }
    }

    LaunchedEffect(loginState.errorMessage) {
        if (loginState.statusCode != 401) {
            loginState.errorMessage?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                viewModel.clearErrorMessage()
            }
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
                Text(text = "Login", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                TextInput(
                    value = loginState.username,
                    onInputChanged = { viewModel.onInputChange("username", it) },
                    label = "Username",
                    errorMessage = loginState.errors["username"]
                )
                PasswordInput(
                    value = loginState.password,
                    onInputChanged = { viewModel.onInputChange("password", it) },
                    errorMessage = loginState.errors["password"]
                )

                if (loginState.statusCode == 401 && loginState.errorMessage != null) {
                    Text(
                        text = loginState.errorMessage.orEmpty(),
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }

                PrimaryButton(
                    text = "Login",
                    isLoading = loginState.isLoading,
                    onClick = {
                        viewModel.login()
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