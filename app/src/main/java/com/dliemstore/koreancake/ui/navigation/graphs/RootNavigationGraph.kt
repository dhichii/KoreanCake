package com.dliemstore.koreancake.ui.navigation.graphs

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dliemstore.koreancake.ui.screens.login.Login
import com.dliemstore.koreancake.ui.screens.main.Main
import com.dliemstore.koreancake.ui.screens.register.Register

object Graph {
    const val ROOT = "root_graph"
    const val MAIN = "main_graph"
    const val CAKE = "cake_graph"
}

enum class RootScreen {
    ROOT_LOGIN,
    ROOT_REGISTER
}

sealed class RootNavigationItem(val route: String) {
    data object Login : RootNavigationItem(RootScreen.ROOT_LOGIN.name)
    data object Register : RootNavigationItem(RootScreen.ROOT_REGISTER.name)
}

@Composable
fun RootNavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = RootNavigationItem.Login.route
    ) {
        composable(route = RootNavigationItem.Login.route) {
            Login(navController)
        }

        composable(route = RootNavigationItem.Register.route) {
            Register(navController)
        }

        composable(route = Graph.MAIN) {
            Main()
        }
    }
}