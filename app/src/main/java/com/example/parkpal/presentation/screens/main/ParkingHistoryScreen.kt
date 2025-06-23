package com.example.parkpal.presentation.screens.main

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.parkpal.presentation.viewmodel.ParkingHistoryViewModel
import com.example.parkpal.presentation.viewmodel.UserViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.parkpal.domain.model.ParkingLocation
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit


@Composable
fun ParkingHistoryScreen(
    userViewModel: UserViewModel,
    parkingHistoryViewModel: ParkingHistoryViewModel = hiltViewModel(),
    onNavigateToProfile: () -> Unit
) {
    val currentUser by userViewModel.currentUser.observeAsState()
    val parkingHistory by parkingHistoryViewModel.currentParkingHistory.collectAsState()
    val locations = parkingHistory?.parkingLocations ?: emptyList()

    LaunchedEffect(currentUser) {
        currentUser?.let {
            parkingHistoryViewModel.fetchParkingHistory(it.userId)
        }
    }

    Log.d("ParkingHistoryScreen", "Current user: $currentUser")
    Log.d("ParkingHistoryScreen", "Parking history: $parkingHistory")

    if (currentUser == null) {
        Text(
            text = "Loading user information...",
            modifier = Modifier.fillMaxSize(),
            textAlign = TextAlign.Center
        )
    } else if (locations.isEmpty()) {
        Text(
            text = "No parking history found.",
            modifier = Modifier.fillMaxSize(),
            textAlign = TextAlign.Center
        )
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(locations) { location ->
                ParkingLocationCard(location)
            }
        }
    }
}

@Composable
fun ParkingLocationCard(location: ParkingLocation) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Address: ${location.address}",
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = "Duration: ${formatDuration(location.duration)}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Text(
                text = "Date: ${formatTimestamp(location.timestamp)}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

fun formatDuration(duration: Long): String {
    Log.d("ParkingLocationCard", "Duration: $duration")
    val hours = TimeUnit.MILLISECONDS.toHours(duration)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(duration) % 60
    return if (hours > 0) {
        "$hours hrs $minutes mins"
    } else {
        "$minutes mins"
    }
}

fun formatTimestamp(timestamp: Long): String {
    val date = java.util.Date(timestamp)
    val format = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    return format.format(date)
}