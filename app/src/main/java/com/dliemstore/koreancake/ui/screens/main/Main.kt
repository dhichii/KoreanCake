package com.dliemstore.koreancake.ui.screens.main

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.dliemstore.koreancake.R
import com.dliemstore.koreancake.ui.navigation.graphs.MainNavigationGraph

data class ScaffoldViewState(
    val topAppBar: TopAppBarItem = TopAppBarItem(),
    val bottomAppBar: @Composable () -> Unit = {},
)

enum class TopAppBarNavigationIcon {
    NONE,
    BACK,
    CLOSE
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
fun Main(navController: NavHostController = rememberNavController()) {
    val scaffoldViewState = remember {
        mutableStateOf(ScaffoldViewState())
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = scaffoldViewState.value.topAppBar.title,
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
        bottomBar = scaffoldViewState.value.bottomAppBar
    ) { contentPadding ->
        MainNavigationGraph(
            navController,
            modifier = Modifier.padding(contentPadding),
            scaffoldViewState
        )
    }
}