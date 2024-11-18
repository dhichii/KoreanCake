package com.dliemstore.koreancake.ui.navigation.graphs

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dliemstore.koreancake.ui.components.BottomNavigationBar
import com.dliemstore.koreancake.ui.components.SaveBottomAppBar
import com.dliemstore.koreancake.ui.screens.add.Add
import com.dliemstore.koreancake.ui.screens.home.Home
import com.dliemstore.koreancake.ui.screens.main.ScaffoldViewState
import com.dliemstore.koreancake.ui.screens.main.TopAppBarItem
import com.dliemstore.koreancake.ui.screens.main.TopAppBarNavigationIcon

enum class MainScreen {
    HOME,
    ADD,
    SETTING,
}

sealed class MainNavigationItem(val route: String) {
    data object Home : MainNavigationItem(MainScreen.HOME.name)
    data object Add : MainNavigationItem(MainScreen.ADD.name)
    data object Setting : MainNavigationItem(MainScreen.SETTING.name)
}

@Composable
fun MainNavigationGraph(
    navController: NavHostController,
    modifier: Modifier,
    scaffoldViewState: MutableState<ScaffoldViewState>
) {
    NavHost(
        navController = navController,
        route = Graph.MAIN,
        startDestination = MainNavigationItem.Home.route,
        modifier = modifier
    ) {
        composable(MainNavigationItem.Home.route) {
            scaffoldViewState.value = ScaffoldViewState(
                topAppBar = TopAppBarItem(),
                bottomAppBar = { BottomNavigationBar(navController) }
            )
            Home(navController)
        }

        composable(
            route = MainNavigationItem.Add.route,
            enterTransition = {
                fadeIn(animationSpec = tween(500)) + slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up, tween(500)
                )
            },
            exitTransition = {
                fadeOut()
            },
            popEnterTransition = {
                fadeIn()
            },
            popExitTransition = {
                fadeOut(animationSpec = tween(500)) + slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down, tween(500)
                )
            }
        ) {
            scaffoldViewState.value = ScaffoldViewState(
                topAppBar = TopAppBarItem(
                    title = { Text("Tambah") },
                    navigationIcon = TopAppBarNavigationIcon.CLOSE
                ),
                bottomAppBar = { SaveBottomAppBar(onClick = {}) }
            )
            Add()
        }

        composable(MainNavigationItem.Setting.route) {
            scaffoldViewState.value = ScaffoldViewState(
                topAppBar = TopAppBarItem(),
                bottomAppBar = { BottomNavigationBar(navController) }
            )
            Text("Setting")
        }
        cakeNavigationGraph(
            navController,
            scaffoldViewState
        )
    }
}