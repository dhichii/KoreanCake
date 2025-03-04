package com.dliemstore.koreancake.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.dliemstore.koreancake.R
import com.dliemstore.koreancake.ui.navigation.graphs.MainNavigationItem
import com.dliemstore.koreancake.ui.navigation.graphs.ProcessNavigationItem
import com.dliemstore.koreancake.ui.navigation.graphs.SettingsNavigationItem

sealed class BottomAppBar {
    data object None : BottomAppBar()
    data object Navigation : BottomAppBar()
    class Save(val onClick: () -> Unit) : BottomAppBar()
}

@Composable
fun BottomAppBar(type: BottomAppBar, navController: NavController) {
    when (type) {
        BottomAppBar.None -> {}
        BottomAppBar.Navigation -> {
            BottomNavigationBar(navController)
        }

        is BottomAppBar.Save -> {
            SaveBottomAppBar(onClick = type.onClick)
        }
    }
}

data class BottomNavigationItem(
    val title: String,
    val route: String,
    val selectedIcon: Int,
    val unselectedIcon: Int,
    val hasNews: Boolean,
    val badgeCount: Int? = null
)

@Composable
fun BottomNavigationBar(navController: NavController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val items = listOf(
        BottomNavigationItem(
            title = "Beranda",
            selectedIcon = R.drawable.home_rounded_filled_24,
            unselectedIcon = R.drawable.home_rounded_24,
            route = MainNavigationItem.Home.route,
            hasNews = false,
        ),
        BottomNavigationItem(
            title = "Tambah",
            selectedIcon = R.drawable.add_circle_rounded_filled_24,
            unselectedIcon = R.drawable.add_circle_rounded_24,
            route = MainNavigationItem.Add.route,
            hasNews = false,
        ),
        BottomNavigationItem(
            title = "Proses",
            selectedIcon = R.drawable.list_alt_rounded_filled_24,
            unselectedIcon = R.drawable.list_alt_rounded_24,
            route = ProcessNavigationItem.Main.route,
            hasNews = false,
        ),
        BottomNavigationItem(
            title = "Pengaturan",
            selectedIcon = R.drawable.settings_rounded_filled_24,
            unselectedIcon = R.drawable.settings_rounded_24,
            route = SettingsNavigationItem.Main.route,
            hasNews = false,
        ),
    )

    if (items.any { it.route == currentRoute }) {
        NavigationBar {
            items.forEach { item ->
                val selected = item.route == currentRoute
                NavigationBarItem(
                    selected = selected,
                    onClick = {
                        if (!selected) {
                            navController.navigate(item.route) {
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
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
                                painter = painterResource(if (selected) item.selectedIcon else item.unselectedIcon),
                                tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
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
