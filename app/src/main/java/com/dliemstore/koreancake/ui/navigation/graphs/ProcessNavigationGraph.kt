package com.dliemstore.koreancake.ui.navigation.graphs

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Reorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.dliemstore.koreancake.ui.components.BottomAppBar
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
            var isReorderEnabled by remember { mutableStateOf(false) }

            scaffoldViewState.value = ScaffoldViewState(
                topAppBar = if (!isReorderEnabled) TopAppBarItem(
                    title = { Text("Proses Pembuatan") },
                    actions = {
                        IconButton(onClick = {
                            navController.navigate(ProcessNavigationItem.Add.route)
                        }) {
                            Icon(
                                imageVector = Icons.Rounded.Add,
                                contentDescription = "Add"
                            )
                        }
                        IconButton(onClick = { isReorderEnabled = true }) {
                            Icon(
                                imageVector = Icons.Rounded.Reorder,
                                contentDescription = "Reorder"
                            )
                        }
                    }
                ) else TopAppBarItem(
                    title = { Text("Urutkan") },
                    navigationIcon = TopAppBarNavigationIcon.Custom(
                        icon = Icons.Rounded.Close,
                        contentDescription = "Close",
                        onClick = { isReorderEnabled = false })
                ),
                bottomAppBar =
                if (!isReorderEnabled) {
                    BottomAppBar.Navigation
                } else {
                    BottomAppBar.Save(onClick = { isReorderEnabled = false })

                }
            )
            Process(navController, isReorderEnabled)
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
            scaffoldViewState.value = ScaffoldViewState(
                topAppBar = TopAppBarItem(
                    title = { Text("Tambah Proses") },
                    navigationIcon = TopAppBarNavigationIcon.CLOSE
                )
            )
            AddProcess(navController)
        }

        composable(
            route = "${ProcessNavigationItem.Edit.route}/{id}",
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

            scaffoldViewState.value = ScaffoldViewState(
                topAppBar = TopAppBarItem(
                    title = { Text("Edit Proses") },
                    navigationIcon = TopAppBarNavigationIcon.CLOSE
                )
            )
            EditProcess(id, navController)
        }
    }
}
