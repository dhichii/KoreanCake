package com.dliemstore.koreancake.ui.navigation.graphs

import androidx.compose.material3.Text
import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.dliemstore.koreancake.ui.components.BottomNavigationBar
import com.dliemstore.koreancake.ui.screens.settings.ChangePassword
import com.dliemstore.koreancake.ui.screens.settings.Settings
import com.dliemstore.koreancake.ui.screens.settings.SettingsForm

enum class SettingsScreen {
    SETTINGS_MAIN,
    SETTINGS_PROFILE,
    SETTINGS_EMAIL,
    SETTINGS_USERNAME,
    SETTINGS_PASSWORD
}

sealed class SettingsNavigationItem(val route: String) {
    data object Main : SettingsNavigationItem(SettingsScreen.SETTINGS_MAIN.name)
    data object Profile : SettingsNavigationItem(SettingsScreen.SETTINGS_PROFILE.name)
    data object Email : SettingsNavigationItem(SettingsScreen.SETTINGS_EMAIL.name)
    data object Username : SettingsNavigationItem(SettingsScreen.SETTINGS_USERNAME.name)
    data object Password : SettingsNavigationItem(SettingsScreen.SETTINGS_PASSWORD.name)
}

sealed class SettingType(
    val fieldLabel: String,
    val route: String
) {
    data object Profile :
        SettingType("Nama", route = SettingsNavigationItem.Profile.route)

    data object Email :
        SettingType("Email Baru", route = SettingsNavigationItem.Email.route)

    data object Username :
        SettingType("Username Baru", route = SettingsNavigationItem.Username.route)
}

fun NavGraphBuilder.settingsNavigationGraph(
    navController: NavHostController,
    scaffoldViewState: MutableState<ScaffoldViewState>
) {
    navigation(
        route = Graph.SETTINGS,
        startDestination = SettingsNavigationItem.Main.route,
    ) {
        composable(route = SettingsNavigationItem.Main.route) {
            scaffoldViewState.value = ScaffoldViewState(
                topAppBar = TopAppBarItem(title = { Text("Settings") }),
                bottomAppBar = { BottomNavigationBar(navController) }
            )
            Settings(navController)
        }

        composable(route = SettingsNavigationItem.Profile.route) {
            scaffoldViewState.value = ScaffoldViewState(
                TopAppBarItem(
                    title = { Text("Profil") },
                    navigationIcon = TopAppBarNavigationIcon.CLOSE
                )
            )
            SettingsForm(SettingType.Profile, navController)
        }

        composable(route = SettingsNavigationItem.Email.route) {
            scaffoldViewState.value = ScaffoldViewState(
                TopAppBarItem(
                    title = { Text("Email") },
                    navigationIcon = TopAppBarNavigationIcon.CLOSE
                )
            )
            SettingsForm(SettingType.Email, navController)
        }

        composable(route = SettingsNavigationItem.Username.route) {
            scaffoldViewState.value = ScaffoldViewState(
                TopAppBarItem(
                    title = { Text("Username") },
                    navigationIcon = TopAppBarNavigationIcon.CLOSE
                )
            )
            SettingsForm(SettingType.Username, navController)
        }

        composable(route = SettingsNavigationItem.Password.route) {
            scaffoldViewState.value = ScaffoldViewState(
                TopAppBarItem(
                    title = { Text("Password") },
                    navigationIcon = TopAppBarNavigationIcon.CLOSE
                )
            )
            ChangePassword(navController)
        }
    }
}
