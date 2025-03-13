package com.dliemstore.koreancake.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

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
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onConfirmation: () -> Unit
) {
    if (!isLoading) {
        CustomDialog(
            text = "Anda ingin keluar?",
            onDismiss = onDismiss,
            dismissButtonText = {
                Text(
                    text = "Batal",
                    color = MaterialTheme.colorScheme.secondary
                )
            },
            onConfirmation = onConfirmation,
            confirmButtonText = { Text(text = "Log Out", color = MaterialTheme.colorScheme.error) },
        )
    } else {
        LoadingDialog(onDismiss)
    }
}

@Composable
fun DeleteDialog(
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onConfirmation: () -> Unit
) {
    if (!isLoading) {
        CustomDialog(
            text = "Ingin menghapus?",
            onDismiss = onDismiss,
            dismissButtonText = {
                Text(
                    text = "Batal",
                    color = MaterialTheme.colorScheme.secondary
                )
            },
            onConfirmation = onConfirmation,
            confirmButtonText = {
                Text(text = "Hapus", color = MaterialTheme.colorScheme.error)
            },
        )
    } else {
        LoadingDialog(onDismiss)
    }
}

@Composable
fun LoadingDialog(onDismiss: () -> Unit = {}) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerLow),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}
