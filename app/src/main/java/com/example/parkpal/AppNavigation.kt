package com.example.parkpal

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.parkpal.presentation.BottomNavDestination
import com.example.parkpal.presentation.BottomNavigationBar
import com.example.parkpal.presentation.screens.main.HomeScreen
import com.example.parkpal.presentation.screens.main.ParkingHistoryScreen
import com.example.parkpal.presentation.screens.main.ProfileScreen
import com.example.parkpal.presentation.screens.onboarding.CarInfoScreen
import com.example.parkpal.presentation.screens.onboarding.GetStartedScreen
import com.example.parkpal.presentation.screens.onboarding.SignUpScreen
import com.example.parkpal.presentation.screens.onboarding.UserInfoScreen
import com.example.parkpal.presentation.screens.onboarding.WelcomeScreen
import com.example.parkpal.presentation.viewmodel.CarViewModel
import com.example.parkpal.presentation.viewmodel.ParkingHistoryViewModel
import com.example.parkpal.presentation.viewmodel.UserViewModel

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    val userViewModel: UserViewModel = hiltViewModel()
    val carViewModel: CarViewModel = hiltViewModel()
    val parkingHistoryViewModel: ParkingHistoryViewModel = hiltViewModel()

    // List of destinations where the Bottom Navigation Bar is visible
    val bottomNavDestinations = listOf(
        BottomNavDestination.MyCar,
        BottomNavDestination.ParkingHistory,
        BottomNavDestination.Profile
    )

    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route

    // Determine if the bottom navigation bar should be shown
    val shouldShowBottomNav = bottomNavDestinations.any { it.route == currentRoute }

    Scaffold(
        bottomBar = {
            if (shouldShowBottomNav) {
                BottomNavigationBar(
                    currentDestination = bottomNavDestinations.find { it.route == currentRoute } ?: BottomNavDestination.MyCar,
                    onDestinationClicked = { destination ->
                        navController.navigate(destination.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "welcome",
            modifier = Modifier.padding(innerPadding)
        ) {
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
                onHomeClick = { navController.navigate(BottomNavDestination.MyCar.route) }
            ) }

            composable("signUp") { SignUpScreen(
                userViewModel = userViewModel,
                onSignUp = { navController.navigate(BottomNavDestination.MyCar.route) },
                onCancel = { navController.navigate("getStarted") }
            ) }

            composable(BottomNavDestination.MyCar.route) { HomeScreen(
                carViewModel = carViewModel,
                parkingHistoryViewModel = parkingHistoryViewModel,
                onNavigateToParkingHistory = { navController.navigate(BottomNavDestination.ParkingHistory.route) }
            ) }

            composable(BottomNavDestination.ParkingHistory.route) { ParkingHistoryScreen(
                userViewModel = userViewModel,
                onNavigateToProfile = { navController.navigate(BottomNavDestination.Profile.route) }
            ) }

            composable(BottomNavDestination.Profile.route) { ProfileScreen(

            ) }
        }
    }
}