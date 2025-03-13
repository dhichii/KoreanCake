package com.dliemstore.koreancake.ui.navigation.graphs

import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
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
            Settings(navController, scaffoldViewState)
        }

        composable(route = SettingsNavigationItem.Profile.route) {
            SettingsForm(SettingType.Profile, navController, scaffoldViewState, "Profil")
        }

        composable(route = SettingsNavigationItem.Email.route) {
            SettingsForm(SettingType.Email, navController, scaffoldViewState, "Email")
        }

        composable(route = SettingsNavigationItem.Username.route) {
            SettingsForm(SettingType.Username, navController, scaffoldViewState, "Username")
        }

        composable(route = SettingsNavigationItem.Password.route) {
            ChangePassword(navController, scaffoldViewState)
        }
    }
}
