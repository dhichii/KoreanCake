package com.dliemstore.koreancake.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dliemstore.koreancake.ui.components.PasswordInput
import com.dliemstore.koreancake.ui.components.PrimaryButton
import com.dliemstore.koreancake.ui.navigation.graphs.ScaffoldViewState
import com.dliemstore.koreancake.ui.navigation.graphs.TopAppBarItem
import com.dliemstore.koreancake.ui.navigation.graphs.TopAppBarNavigationIcon

@Composable
fun ChangePassword(
    navController: NavController,
    scaffoldViewState: MutableState<ScaffoldViewState>
) {
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }

    val hasMinimum: Boolean = password.matches(Regex(".{8,}"))
    val isMatched: Boolean = confirmPassword == password
    var isConfirmPasswordChanged by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        scaffoldViewState.value = ScaffoldViewState(
            TopAppBarItem(
                title = { Text("Password") },
                navigationIcon = TopAppBarNavigationIcon.CLOSE
            )
        )
    }

    Column(modifier = Modifier.padding(horizontal = 12.dp)) {
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
            )
            if (!isMatched && isConfirmPasswordChanged) Text("Konfirmasi password tidak sama")
        }

        Spacer(modifier = Modifier.height(16.dp))
        PrimaryButton(
            text = "Simpan",
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        )
    }
}