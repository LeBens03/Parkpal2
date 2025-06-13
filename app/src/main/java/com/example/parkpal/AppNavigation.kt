package com.example.parkpal

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.parkpal.presentation.screens.onboarding.UserInfoScreen
import com.example.parkpal.presentation.screens.onboarding.WelcomeScreen

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = "welcome") {
        composable("welcome") { WelcomeScreen(
            onContinueClicked = { navController.navigate("userInfo") }
        ) }
        composable("userInfo") { UserInfoScreen() }
    }
}