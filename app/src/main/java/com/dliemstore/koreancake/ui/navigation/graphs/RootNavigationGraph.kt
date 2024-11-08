package com.dliemstore.koreancake.ui.navigation.graphs

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dliemstore.koreancake.ui.screens.main.Main

object Graph {
    const val ROOT = "root_graph"
    const val MAIN = "main_graph"
    const val CAKE = "cake_graph"
}

@Composable
fun RootNavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Graph.MAIN
    ) {
        composable(route = Graph.MAIN) {
            Main()
        }
    }
}