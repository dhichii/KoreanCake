package com.dliemstore.koreancake.ui.screens.main

import androidx.compose.foundation.layout.RowScope
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.dliemstore.koreancake.ui.components.BottomNavigationBar
import com.dliemstore.koreancake.ui.navigation.graphs.MainNavigationGraph

data class ScaffoldViewState(
    val topAppBar: TopAppBarItem = TopAppBarItem(),
    val bottomNavBar: String? = "main",
)

enum class TopAppBarNavigationIcon {
    NONE,
    BACK,
    CLOSE
}

data class TopAppBarItem(
    val title: String? = "Korean Cake",
    val navigationIcon: TopAppBarNavigationIcon = TopAppBarNavigationIcon.NONE,
    val actions: @Composable RowScope.() -> Unit = {}
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Main(navController: NavHostController = rememberNavController()) {
    val scaffoldViewState = remember {
        mutableStateOf(ScaffoldViewState())
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { scaffoldViewState.value.topAppBar.title?.let { Text(text = it) } },
                navigationIcon = {
                    when (scaffoldViewState.value.topAppBar.navigationIcon) {
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
                    }
                },
                actions = scaffoldViewState.value.topAppBar.actions,
            )
        },
        bottomBar = { BottomNavigationBar(navController) }
    ) { contentPadding ->
        MainNavigationGraph(
            navController,
            modifier = Modifier.padding(contentPadding),
            scaffoldViewState
        )
    }
}