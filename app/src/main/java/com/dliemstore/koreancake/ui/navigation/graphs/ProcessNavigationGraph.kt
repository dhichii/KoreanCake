package com.dliemstore.koreancake.ui.navigation.graphs

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.dliemstore.koreancake.ui.screens.process.AddProcess
import com.dliemstore.koreancake.ui.screens.process.EditProcess
import com.dliemstore.koreancake.ui.screens.process.Process

enum class ProcessScreen {
    PROCESS_MAIN,
    PROCESS_ADD,
    PROCESS_EDIT
}

sealed class ProcessNavigationItem(val route: String) {
    data object Main : ProcessNavigationItem(ProcessScreen.PROCESS_MAIN.name)
    data object Add : ProcessNavigationItem(ProcessScreen.PROCESS_ADD.name)
    data object Edit : ProcessNavigationItem(ProcessScreen.PROCESS_EDIT.name)
}

fun NavGraphBuilder.processNavigationGraph(
    navController: NavHostController,
    scaffoldViewState: MutableState<ScaffoldViewState>
) {
    navigation(
        route = Graph.PROCESS,
        startDestination = ProcessNavigationItem.Main.route,
    ) {
        composable(route = ProcessNavigationItem.Main.route) {
            Process(navController, scaffoldViewState)
        }

        composable(
            route = ProcessNavigationItem.Add.route,
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
            AddProcess(navController, scaffoldViewState)
        }

        composable(
            route = "${ProcessNavigationItem.Edit.route}/{id}/{name}/{step}",
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
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")!!
            EditProcess(id, navController, scaffoldViewState)
        }
    }
}
