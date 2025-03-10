package com.dliemstore.koreancake.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ErrorState(errorMessage: String?, onRetry: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Gagal memuat data",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        errorMessage?.let {
            Text(text = it, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(8.dp))
        }
        PrimaryButton("Refresh", onClick = onRetry)
    }
}