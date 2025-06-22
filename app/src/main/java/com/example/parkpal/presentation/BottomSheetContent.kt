package com.example.parkpal.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.Locale

@Composable
fun BottomSheetContent(
    distance: Float?,
    address: String?,
    onNavigateClick: () -> Unit,
    onArrivedClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Parking Details",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Distance: ${distance?.let { String.format(Locale.getDefault(), "%.2f km", it) } ?: "Unknown"}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Address: ${address ?: "Unknown"}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(
            onClick = onNavigateClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Navigate with Google Maps")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onArrivedClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("I Arrived")
        }
    }
}