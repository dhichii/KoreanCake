package com.dliemstore.koreancake.ui.navigation.graphs

import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.dliemstore.koreancake.ui.screens.login.Login
import com.dliemstore.koreancake.ui.screens.register.Register

enum class AuthScreen {
    AUTH_LOGIN,
    AUTH_REGISTER
}

sealed class AuthNavigationItem(val route: String) {
    data object Login : AuthNavigationItem(AuthScreen.AUTH_LOGIN.name)
    data object Register : AuthNavigationItem(AuthScreen.AUTH_REGISTER.name)
}

fun NavGraphBuilder.authNavigationGraph(
    navController: NavHostController,
    scaffoldViewState: MutableState<ScaffoldViewState>
) {
    navigation(
        route = Graph.AUTH,
        startDestination = AuthNavigationItem.Login.route,
    ) {
        composable(route = AuthNavigationItem.Login.route) {
            Login(navController, scaffoldViewState)
        }

        composable(route = AuthNavigationItem.Register.route) {
            Register(navController, scaffoldViewState)
        }
    }
}
