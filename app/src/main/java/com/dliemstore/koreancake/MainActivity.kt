package com.dliemstore.koreancake

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dliemstore.koreancake.ui.components.BottomNavigationBar
import com.dliemstore.koreancake.ui.components.BottomNavigationItem
import com.dliemstore.koreancake.ui.theme.KoreanCakeTheme

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KoreanCakeTheme {
                val items = listOf(
                    BottomNavigationItem(
                        title = "Home",
                        selectedIcon = Icons.Filled.Home,
                        unselectedIcon = Icons.Outlined.Home,
                        route = "home",
                        hasNews = false,
                    ),
                    BottomNavigationItem(
                        title = "Add",
                        selectedIcon = Icons.Filled.AddCircle,
                        unselectedIcon = Icons.Outlined.AddCircle,
                        route = "add",
                        hasNews = false,
                    ),
                    BottomNavigationItem(
                        title = "Setting",
                        selectedIcon = Icons.Filled.Settings,
                        unselectedIcon = Icons.Outlined.Settings,
                        route = "setting",
                        hasNews = false,
                    ),
                )

                val navController = rememberNavController()

                Surface(modifier = Modifier.fillMaxSize()) {
                    Scaffold(
                        topBar = { TopAppBar(title = { Text("Korean Cake") }) },
                        bottomBar = {
                            BottomNavigationBar(
                                items,
                                navController,
                                onItemClick = {
                                    navController.navigate(it.route)
                                }
                            )
                        }
                    ) { contentPadding ->
                        Navigation(navController, modifier = Modifier.padding(contentPadding))
                    }
                }
            }
        }
    }
}

@Composable
fun Navigation(navController: NavHostController, modifier: Modifier) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable("home") {
            Text("Add")
        }

        composable("add") {
            Text("Add")
        }

        composable("setting") {
            Text("Setting")
        }
    }
}