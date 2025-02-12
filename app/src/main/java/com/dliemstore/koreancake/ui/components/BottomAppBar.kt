package com.dliemstore.koreancake.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.dliemstore.koreancake.ui.navigation.graphs.MainNavigationItem
import com.dliemstore.koreancake.ui.navigation.graphs.ProcessNavigationItem
import com.dliemstore.koreancake.ui.navigation.graphs.SettingsNavigationItem

data class BottomNavigationItem(
    val title: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = null
)

@Composable
fun BottomNavigationBar(navController: NavController) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry.value?.destination?.route
    val items = listOf(
        BottomNavigationItem(
            title = "Beranda",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            route = MainNavigationItem.Home.route,
            hasNews = false,
        ),
        BottomNavigationItem(
            title = "Tambah",
            selectedIcon = Icons.Filled.AddCircle,
            unselectedIcon = Icons.Outlined.AddCircle,
            route = MainNavigationItem.Add.route,
            hasNews = false,
        ),
        BottomNavigationItem(
            title = "Proses",
            selectedIcon = Icons.Filled.Checklist,
            unselectedIcon = Icons.Outlined.Checklist,
            route = ProcessNavigationItem.Main.route,
            hasNews = false,
        ),
        BottomNavigationItem(
            title = "Pengaturan",
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
            route = SettingsNavigationItem.Main.route,
            hasNews = false,
        ),
    )

    if (items.any { it.route == currentDestination }) {
        NavigationBar {
            items.forEach { item ->
                val selected = item.route == backStackEntry.value?.destination?.route
                NavigationBarItem(
                    selected = selected,
                    onClick = {
                        navController.navigate(item.route)
                    },
                    label = { Text(item.title) },
                    icon = {
                        BadgedBox(
                            badge = {
                                if (item.badgeCount != null) {
                                    Badge {
                                        Text(item.badgeCount.toString())
                                    }
                                } else if (item.hasNews) {
                                    Badge()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.title
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun SaveBottomAppBar(onClick: () -> Unit) {
    BottomAppBar {
        Button(
            onClick = onClick,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp)
        ) {
            Text("Save")
        }
    }
}
