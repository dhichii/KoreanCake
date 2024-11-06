package com.dliemstore.koreancake.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dliemstore.koreancake.ui.screens.cake.DetailCake
import com.dliemstore.koreancake.ui.screens.home.Home

enum class Screen {
    HOME,
    ADD,
    SETTING,
    CAKE
}

sealed class NavigationItem(val route: String) {
    data object Home : NavigationItem(Screen.HOME.name)
    data object Add : NavigationItem(Screen.ADD.name)
    data object Setting : NavigationItem(Screen.SETTING.name)
    data object Cake : NavigationItem(Screen.CAKE.name)
}

@Composable
fun Navigation(navController: NavHostController, modifier: Modifier) {
    NavHost(
        navController = navController,
        startDestination = NavigationItem.Home.route,
        modifier = modifier
    ) {
        composable(NavigationItem.Home.route) {
            Home(navController)
        }

        composable(NavigationItem.Cake.route + "/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")!!
            DetailCake(navController, id)
        }

        composable(NavigationItem.Add.route) {
            Text("Add")
        }

        composable(NavigationItem.Setting.route) {
            Text("Setting")
        }
    }
}