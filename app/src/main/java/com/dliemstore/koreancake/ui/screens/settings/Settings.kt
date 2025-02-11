package com.dliemstore.koreancake.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dliemstore.koreancake.ui.components.LogoutDialog
import com.dliemstore.koreancake.ui.navigation.graphs.Graph
import com.dliemstore.koreancake.ui.navigation.graphs.SettingsNavigationItem

data class SettingsItem(val text: String, val route: String)

@Composable
fun Settings(navController: NavController) {
    val settingsOptions = listOf(
        SettingsItem("Ubah Profil", SettingsNavigationItem.Profile.route),
        SettingsItem("Ubah Email", SettingsNavigationItem.Email.route),
        SettingsItem("Ubah Username", SettingsNavigationItem.Username.route),
        SettingsItem("Ubah Password", SettingsNavigationItem.Password.route)
    )

    Column {
        // profile
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(text = "My Name", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(text = "@example")
            Text(text = "example@gmail.com")
        }

        // settings
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.padding(12.dp, 8.dp)
        ) {
            Column {
                Text(
                    "Settings",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 12.dp)
                )
                LazyColumn {
                    items(settingsOptions) { setting ->
                        SettingListItem(
                            setting.text,
                            onClick = { navController.navigate(setting.route) }
                        )
                    }
                }
            }
        }

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.padding(12.dp, 8.dp)
        ) {
            // logout
            var isShowLogoutDialog by remember { mutableStateOf(false) }
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    isShowLogoutDialog = true
                }
                .padding(12.dp)
            ) {
                Text(text = "Logout", color = MaterialTheme.colorScheme.error)
                Icon(
                    imageVector = Icons.Filled.ChevronRight,
                    contentDescription = "navigate setting"
                )

                if (isShowLogoutDialog) {
                    LogoutDialog(
                        onDismiss = { isShowLogoutDialog = false },
                        onConfirmation = {
                            isShowLogoutDialog = false
                            navController.navigate(Graph.AUTH) {
                                popUpTo(Graph.MAIN) { inclusive = true }
                            }
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun SettingListItem(text: String, onClick: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Text(text = text)
        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = text
        )
    }
}