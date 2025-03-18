package com.dliemstore.koreancake.ui.screens.settings

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dliemstore.koreancake.data.api.TokenManager
import com.dliemstore.koreancake.ui.components.BottomAppBar
import com.dliemstore.koreancake.ui.components.LoadingDialog
import com.dliemstore.koreancake.ui.components.LogoutDialog
import com.dliemstore.koreancake.ui.navigation.graphs.Graph
import com.dliemstore.koreancake.ui.navigation.graphs.ScaffoldViewState
import com.dliemstore.koreancake.ui.navigation.graphs.SettingsNavigationItem
import com.dliemstore.koreancake.ui.navigation.graphs.TopAppBarItem
import com.dliemstore.koreancake.ui.viewmodel.settings.SettingsViewModel
import com.dliemstore.koreancake.util.JWTUtils
import com.dliemstore.koreancake.util.Resource
import com.dliemstore.koreancake.util.ToastUtils

data class SettingsItem(val text: String, val route: String)

@Composable
fun Settings(
    navController: NavController,
    scaffoldViewState: MutableState<ScaffoldViewState>,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val tokenManager = TokenManager(context)
    val user = JWTUtils.decodePayload(tokenManager.getToken())

    val logoutState by viewModel.logoutState.collectAsState()

    val isShowLogoutDialog = remember { mutableStateOf(false) }
    val isLoggingOut = logoutState is Resource.Loading

    LaunchedEffect(Unit) {
        scaffoldViewState.value = ScaffoldViewState(
            topAppBar = TopAppBarItem(title = { Text("Settings") }),
            bottomAppBar = BottomAppBar.Navigation
        )
    }

    HandleStates(context = context, navController = navController, logoutState = logoutState)

    Column(Modifier.fillMaxSize()) {
        SettingProfile(
            name = user?.optString("name"),
            username = user?.optString("username"),
            email = user?.optString("email"),
        )
        SettingsList(navController)
        SettingLogout { isShowLogoutDialog.value = true }

        if (isShowLogoutDialog.value) {
            LogoutDialog(
                onDismiss = { isShowLogoutDialog.value = false },
                onConfirmation = {
                    isShowLogoutDialog.value = false
                    viewModel.logout()
                },
            )
        }

        if (isLoggingOut) LoadingDialog()
    }
}

@Composable
fun HandleStates(
    context: Context,
    navController: NavController,
    logoutState: Resource<Unit>?
) {
    LaunchedEffect(logoutState) {
        when (logoutState) {
            is Resource.Success -> {
                navController.navigate(Graph.AUTH) {
                    popUpTo(Graph.MAIN) { inclusive = true }
                }
            }

            is Resource.Error -> {
                ToastUtils.show(context, logoutState.msg ?: "Gagal Logout")
            }

            else -> {}
        }
    }
}

@Composable
fun SettingProfile(name: String?, username: String?, email: String?) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Text(
            text = name ?: "-",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Text(text = "@$username")
        Text(text = email ?: "-")
    }
}

@Composable
fun SettingsList(navController: NavController) {
    val settingsOptions = listOf(
        SettingsItem("Ubah Profil", SettingsNavigationItem.Profile.route),
        SettingsItem("Ubah Email", SettingsNavigationItem.Email.route),
        SettingsItem("Ubah Username", SettingsNavigationItem.Username.route),
        SettingsItem("Ubah Password", SettingsNavigationItem.Password.route)
    )

    SettingSection(title = "Settings", content = {
        LazyColumn {
            items(settingsOptions) { setting ->
                SettingListItem(
                    setting.text,
                    onClick = { navController.navigate(setting.route) }
                )
            }
        }
    })
}

@Composable
fun SettingLogout(onClick: () -> Unit) {
    SettingSection {
        SettingListItem(
            title = "Logout",
            titleColor = MaterialTheme.colorScheme.error,
            onClick = onClick
        )
    }
}

@Composable
fun SettingSection(title: String? = null, content: @Composable () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.padding(12.dp, 8.dp)
    ) {
        Column {
            title?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 12.dp)
                )
            }
            content()
        }
    }
}

@Composable
fun SettingListItem(
    title: String,
    titleColor: Color = Color.Unspecified,
    onClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Text(text = title, color = titleColor)
        Icon(
            imageVector = Icons.Rounded.ChevronRight,
            contentDescription = title
        )
    }
}