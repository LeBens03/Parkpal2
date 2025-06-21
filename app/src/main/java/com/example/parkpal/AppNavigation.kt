package com.example.parkpal

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.parkpal.presentation.screens.main.HomeScreen
import com.example.parkpal.presentation.screens.onboarding.CarInfoScreen
import com.example.parkpal.presentation.screens.onboarding.GetStartedScreen
import com.example.parkpal.presentation.screens.onboarding.SignUpScreen
import com.example.parkpal.presentation.screens.onboarding.UserInfoScreen
import com.example.parkpal.presentation.screens.onboarding.WelcomeScreen
import com.example.parkpal.presentation.viewmodel.CarViewModel
import com.example.parkpal.presentation.viewmodel.UserViewModel

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {

    val userViewModel: UserViewModel = hiltViewModel()
    val carViewModel: CarViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = "welcome") {
        composable("welcome") { WelcomeScreen(
            onContinueClicked = { navController.navigate("userInfo") }
        ) }

        composable("userInfo") { UserInfoScreen(
            userViewModel = userViewModel,
            onSaveUser = { navController.navigate("carInfo") }
        ) }

        composable("carInfo") { CarInfoScreen(
            userViewModel = userViewModel,
            carViewModel = carViewModel,
            onCarSaved = { navController.navigate("getStarted") }
        ) }

        composable("getStarted") { GetStartedScreen(
            onSignUpClick = { navController.navigate("signUp") },
            onHomeClick = { navController.navigate("home") }
        ) }

        composable("signUp") { SignUpScreen(
            userViewModel = userViewModel,
            onSignUp = { navController.navigate("home") },
            onCancel = { navController.navigate("getStarted") }
        ) }

        composable("home") { HomeScreen(
            carViewModel = carViewModel
        ) }
    }
}