package com.dliemstore.koreancake.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun CustomDialog(
    title: String? = null,
    text: String? = null,
    icon: ImageVector? = null,
    onDismiss: () -> Unit,
    dismissButtonText: @Composable () -> Unit,
    onConfirmation: () -> Unit,
    confirmButtonText: @Composable () -> Unit
) {
    AlertDialog(
        icon = {
            if (icon != null) {
                Icon(icon, contentDescription = "Example Icon")
            }
        },
        title = title?.let {
            {
                Text(text = title)
            }
        },
        text = text?.let {
            {
                Text(text)
            }
        },
        onDismissRequest = {
            onDismiss()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                confirmButtonText()
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                dismissButtonText()
            }
        }
    )
}

@Composable
fun LogoutDialog(
    onDismiss: () -> Unit,
    onConfirmation: () -> Unit
) {
    CustomDialog(
        text = "Anda ingin keluar?",
        onDismiss = onDismiss,
        dismissButtonText = { Text(text = "Batal", color = MaterialTheme.colorScheme.secondary) },
        onConfirmation = onConfirmation,
        confirmButtonText = { Text(text = "Log Out", color = MaterialTheme.colorScheme.error) },
    )
}

@Composable
fun DeleteDialog(
    onDismiss: () -> Unit,
    onConfirmation: () -> Unit
) {
    CustomDialog(
        text = "Ingin menghapus?",
        onDismiss = onDismiss,
        dismissButtonText = { Text(text = "Batal", color = MaterialTheme.colorScheme.secondary) },
        onConfirmation = onConfirmation,
        confirmButtonText = { Text(text = "Hapus", color = MaterialTheme.colorScheme.error) },
    )
}
