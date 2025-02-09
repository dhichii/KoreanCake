package com.dliemstore.koreancake.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PrimaryButton(
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { onClick() },
        shape = MaterialTheme.shapes.medium,
        enabled = enabled,
        modifier = modifier
    ) {
        Text(text)
    }
}

@Composable
fun SecondaryButton(
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = { onClick() },
        shape = MaterialTheme.shapes.medium,
        enabled = enabled,
        modifier = modifier
    ) {
        Text(text)
    }
}