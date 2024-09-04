package com.example.jetbookreader.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jetbookreader.screens.ReaderSplashScreen
import com.example.jetbookreader.screens.home.Home
import com.example.jetbookreader.screens.login.ReaderLoginScreen
import com.example.jetbookreader.screens.search.ReaderSearchScreen
import com.example.jetbookreader.screens.stats.ReaderStatsScreen
import com.example.jetbookreader.screens.update.ReaderUpdateScreen

@Composable
fun ReaderNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = ReaderScreens.SplashScreen.name
    ) {
        composable(ReaderScreens.SplashScreen.name) {
            ReaderSplashScreen(navController = navController)
        }
        composable(ReaderScreens.ReaderHomeScreen.name) {
            Home(navController = navController)
        }
        composable(ReaderScreens.LoginScreen.name) {
            ReaderLoginScreen(navController = navController)
        }

        composable(ReaderScreens.SearchScreen.name) {
            ReaderSearchScreen(navController = navController)
        }
        composable(ReaderScreens.ReaderStatsScreen.name) {
            ReaderStatsScreen(navController = navController)
        }
        composable(ReaderScreens.UpdateScreen.name) {
            ReaderUpdateScreen(navController = navController)
        }
    }
}