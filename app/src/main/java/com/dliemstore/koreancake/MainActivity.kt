package com.dliemstore.koreancake

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.dliemstore.koreancake.data.api.TokenManager
import com.dliemstore.koreancake.ui.navigation.graphs.RootNavigationGraph
import com.dliemstore.koreancake.ui.theme.KoreanCakeTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val isLoggedIn = tokenManager.getToken() != null

            KoreanCakeTheme {
                RootNavigationGraph(isLoggedIn, navController)
            }
        }
    }
}
