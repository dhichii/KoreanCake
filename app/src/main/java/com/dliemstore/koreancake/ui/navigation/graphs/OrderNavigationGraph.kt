package com.dliemstore.koreancake.ui.navigation.graphs

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.dliemstore.koreancake.ui.components.BottomAppBar
import com.dliemstore.koreancake.ui.screens.order.DetailOrder
import com.dliemstore.koreancake.ui.screens.order.edit.EditOrder

enum class OrderScreen {
    ORDER_DETAIL,
    ORDER_EDIT
}

sealed class OrderNavigationItem(val route: String) {
    data object Detail : OrderNavigationItem(OrderScreen.ORDER_DETAIL.name)
    data object Edit : OrderNavigationItem(OrderScreen.ORDER_EDIT.name)
}

fun NavGraphBuilder.orderNavigationGraph(
    navController: NavHostController,
    scaffoldViewState: MutableState<ScaffoldViewState>
) {
    navigation(
        route = Graph.ORDER,
        startDestination = "${OrderNavigationItem.Detail.route}/{id}",
    ) {
        composable(
            route = "${OrderNavigationItem.Detail.route}/{id}",
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(500)
                )
            },
            exitTransition = {
                fadeOut()
            },
            popEnterTransition = {
                fadeIn()
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(500)
                )
            }
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")!!

            scaffoldViewState.value = ScaffoldViewState(
                topAppBar = TopAppBarItem(
                    title = { Text("Detail") },
                    navigationIcon = TopAppBarNavigationIcon.BACK,
                    actions = {
                        IconButton(onClick = {
                            navController.navigate("${OrderNavigationItem.Edit.route}/$id")
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = "Edit"
                            )
                        }
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Delete"
                            )
                        }
                    }
                )
            )

            DetailOrder(id)
        }
    }

    composable(
        route = "${OrderNavigationItem.Edit.route}/{id}",
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
        scaffoldViewState.value = ScaffoldViewState(
            topAppBar = TopAppBarItem(
                title = { Text("Edit") },
                navigationIcon = TopAppBarNavigationIcon.CLOSE
            ),
            bottomAppBar = BottomAppBar.Save(onClick = {})
        )
        val id = backStackEntry.arguments?.getString("id")!!

        EditOrder(id)
    }
}
