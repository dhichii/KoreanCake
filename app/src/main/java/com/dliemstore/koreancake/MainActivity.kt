package com.dliemstore.koreancake

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.dliemstore.koreancake.ui.navigation.graphs.RootNavigationGraph
import com.dliemstore.koreancake.ui.theme.KoreanCakeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            KoreanCakeTheme {
                RootNavigationGraph(navController = rememberNavController())
            }
        }
    }
}
