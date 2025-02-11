package com.dliemstore.koreancake.ui.navigation.graphs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Reorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.dliemstore.koreancake.ui.components.BottomNavigationBar

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
            scaffoldViewState.value = ScaffoldViewState(
                topAppBar = TopAppBarItem(
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
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Filled.Reorder,
                                contentDescription = "Reorder"
                            )
                        }
                    }
                ),
                bottomAppBar = { BottomNavigationBar(navController) }
            )
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
