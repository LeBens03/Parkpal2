package com.example.parkpal.presentation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource

@Composable
fun BottomNavigationBar(
    currentDestination: BottomNavDestination,
    onDestinationClicked: (BottomNavDestination) -> Unit
) {
    NavigationBar {
        listOf(
            BottomNavDestination.MyCar,
            BottomNavDestination.ParkingHistory,
            BottomNavDestination.Profile
        ).forEach { destination ->
            NavigationBarItem(
                icon = { Icon(painter = painterResource(id = destination.icon), contentDescription = destination.label) },
                label = { Text(destination.label) },
                selected = currentDestination.route == destination.route,
                onClick = { onDestinationClicked(destination) }
            )
        }
    }
}