package com.dliemstore.koreancake.ui.navigation.graphs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Reorder
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
import com.dliemstore.koreancake.ui.components.BottomNavigationBar
import com.dliemstore.koreancake.ui.components.SaveBottomAppBar
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
                    title = { Text("Process") },
                    actions = {
                        IconButton(onClick = {
                            navController.navigate(ProcessNavigationItem.Add.route)
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "Add"
                            )
                        }
                        IconButton(onClick = { isReorderEnabled = true }) {
                            Icon(
                                imageVector = Icons.Filled.Reorder,
                                contentDescription = "Reorder"
                            )
                        }
                    }
                ) else TopAppBarItem(
                    title = { Text("Process") },
                    navigationIcon = TopAppBarNavigationIcon.Custom(
                        icon = Icons.Filled.Close,
                        contentDescription = "Close",
                        onClick = { isReorderEnabled = false })
                ),
                bottomAppBar = {
                    if (!isReorderEnabled) {
                        BottomNavigationBar(
                            navController
                        )
                    } else {
                        SaveBottomAppBar(onClick = { isReorderEnabled = false })
                    }
                }
            )
            Process(navController, isReorderEnabled)
        }

        composable(route = ProcessNavigationItem.Add.route) {
            scaffoldViewState.value = ScaffoldViewState()
            Text("Add")
        }

        composable(route = "${ProcessNavigationItem.Edit.route}/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")!!

            scaffoldViewState.value = ScaffoldViewState()
            Text("Edit $id")
        }
    }
}
