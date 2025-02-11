package com.dliemstore.koreancake.ui.navigation.graphs

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.dliemstore.koreancake.R

object Graph {
    const val ROOT = "root_graph"
    const val AUTH = "auth_graph"
    const val MAIN = "main_graph"
    const val CAKE = "cake_graph"
    const val PROCESS = "process_graph"
    const val SETTINGS = "settings_graph"
}

data class ScaffoldViewState(
    val topAppBar: TopAppBarItem? = null,
    val bottomAppBar: @Composable () -> Unit = {},
)

sealed class TopAppBarNavigationIcon {
    data object NONE : TopAppBarNavigationIcon()
    data object BACK : TopAppBarNavigationIcon()
    data object CLOSE : TopAppBarNavigationIcon()
    class Custom(val icon: ImageVector, val contentDescription: String, val onClick: () -> Unit) :
        TopAppBarNavigationIcon()
}

@Composable
fun TopAppBarNavigationIcon(type: TopAppBarNavigationIcon, navController: NavController) {
    when (type) {
        TopAppBarNavigationIcon.NONE -> {}
        TopAppBarNavigationIcon.BACK ->
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }

        TopAppBarNavigationIcon.CLOSE ->
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Back"
                )
            }

        is TopAppBarNavigationIcon.Custom ->
            IconButton(onClick = type.onClick) {
                Icon(
                    imageVector = type.icon,
                    contentDescription = type.contentDescription
                )
            }
    }
}

data class TopAppBarItem(
    val title: @Composable () -> Unit = {
        Text(
            text = stringResource(R.string.app_name),
            fontFamily = FontFamily(
                Font(R.font.more_sugar_regular)
            ),
            modifier = Modifier.height(22.dp)
        )
    },
    val navigationIcon: TopAppBarNavigationIcon = TopAppBarNavigationIcon.NONE,
    val actions: @Composable RowScope.() -> Unit = {}
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RootNavigationGraph(isLoggedIn: Boolean, navController: NavHostController) {
    val scaffoldViewState = remember {
        mutableStateOf(ScaffoldViewState())
    }
    Scaffold(
        topBar = {
            scaffoldViewState.value.topAppBar?.let { topAppBar ->
                TopAppBar(
                    title = topAppBar.title,
                    navigationIcon = {
                        TopAppBarNavigationIcon(
                            topAppBar.navigationIcon,
                            navController
                        )
                    },
                    actions = topAppBar.actions,
                )
            }
        },
        bottomBar = scaffoldViewState.value.bottomAppBar
    ) { contentPadding ->
        NavHost(
            navController = navController,
            route = Graph.ROOT,
            startDestination = if (isLoggedIn) Graph.MAIN else Graph.AUTH,
            modifier = Modifier.padding(contentPadding)
        ) {
            authNavigationGraph(navController, scaffoldViewState)
            mainNavigationGraph(navController, scaffoldViewState)
            cakeNavigationGraph(navController, scaffoldViewState)
            processNavigationGraph(navController, scaffoldViewState)
            settingsNavigationGraph(navController, scaffoldViewState)
        }
    }
}