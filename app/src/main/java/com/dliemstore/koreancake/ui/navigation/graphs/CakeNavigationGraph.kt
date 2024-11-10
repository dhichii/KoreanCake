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
import com.dliemstore.koreancake.ui.screens.cake.DetailCake
import com.dliemstore.koreancake.ui.screens.main.ScaffoldViewState
import com.dliemstore.koreancake.ui.screens.main.TopAppBarItem
import com.dliemstore.koreancake.ui.screens.main.TopAppBarNavigationIcon

enum class Screen {
    CAKE_DETAIL,
    CAKE_EDIT
}

sealed class CakeNavigationItem(val route: String) {
    data object Detail : CakeNavigationItem(Screen.CAKE_DETAIL.name)
    data object Edit : CakeNavigationItem(Screen.CAKE_EDIT.name)
}

fun NavGraphBuilder.cakeNavigationGraph(
    navController: NavHostController,
    scaffoldViewState: MutableState<ScaffoldViewState>
) {
    navigation(
        route = Graph.CAKE,
        startDestination = "${CakeNavigationItem.Detail.route}/{id}",
    ) {
        composable(
            route = "${CakeNavigationItem.Detail.route}/{id}",
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
                    title = "Detail",
                    navigationIcon = TopAppBarNavigationIcon.BACK,
                    actions = {
                        IconButton(onClick = {
                            navController.navigate("${CakeNavigationItem.Edit.route}/$id")
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

            DetailCake(id)
        }
    }

    composable(
        route = "${CakeNavigationItem.Edit.route}/{id}",
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
                title = "Edit",
                navigationIcon = TopAppBarNavigationIcon.CLOSE
            )
        )
        val id = backStackEntry.arguments?.getString("id")!!

        Text("Edit $id")
    }
}
