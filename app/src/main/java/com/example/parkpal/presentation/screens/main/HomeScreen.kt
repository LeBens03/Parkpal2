package com.example.parkpal.presentation.screens.main

import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.example.parkpal.presentation.viewmodel.MapViewModel
import com.google.maps.android.compose.GoogleMap
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.parkpal.domain.model.ParkingLocation
import com.example.parkpal.presentation.MapEvent
import com.example.parkpal.presentation.viewmodel.CarViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun HomeScreen(
    mapViewModel: MapViewModel = hiltViewModel(),
    carViewModel: CarViewModel
) {
    val state = mapViewModel.state
    val uiSettings = remember { MapUiSettings(
        zoomControlsEnabled = false,
    ) }
    val currentCar by carViewModel.currentCar.observeAsState()

    Log.d("HomeScreen", "Current car: $currentCar")
    Log.d("HomeScreen", "User location: ${state.userLocation}")
    Log.d("HomeScreen", "Parking location: ${state.parkingLocation}")

    var permissionsGranted by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val requestPermissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            permissionsGranted = permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                    permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true
        }
    )

    LaunchedEffect(Unit) {
        val fineLocationStatus = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
        val coarseLocationStatus = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (fineLocationStatus == PackageManager.PERMISSION_GRANTED ||
            coarseLocationStatus == PackageManager.PERMISSION_GRANTED
        ) {
            permissionsGranted = true
        } else {
            requestPermissionsLauncher.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    LaunchedEffect(permissionsGranted) {
        if (permissionsGranted) {
            mapViewModel.startLocationUpdates()
            Log.d("HomeScreen", "Permissions granted")
        } else {
            Log.e("HomeScreen", "Permissions not granted")
        }
    }

    val parkingMarkerState = state.parkingLocation?.let { rememberMarkerState(position = LatLng(it.latitude, it.longitude)) }

    val defaultCityLatLng = LatLng(45.0703, 7.6869)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultCityLatLng, 12f) //
    }

    LaunchedEffect(state.userLocation) {
        state.userLocation?.let { userLoc ->
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(userLoc, 15f)
            cameraPositionState.animate(cameraUpdate, 1000)
        }
    }

    Scaffold (
        floatingActionButton = {
            if (state.parkingLocation == null && state.userLocation != null) {
                FloatingActionButton(
                    onClick = {
                        state.userLocation.let { location ->
                            currentCar?.let { car ->
                                val parkingLocation = ParkingLocation(
                                    latitude = location.latitude,
                                    longitude = location.longitude,
                                    carId = car.carId,
                                    userId = car.userId,
                                    timestamp = System.currentTimeMillis().toString()
                                )
                                mapViewModel.onEvent(
                                    MapEvent.OnParkMyCarClicked(parkingLocation)
                                )
                            }
                        }
                    },
                ) {
                    Text("Park My Car")
                }
            }
        }
    ){ paddingValues ->
        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            properties = mapViewModel.state.properties,
            cameraPositionState = cameraPositionState,
            uiSettings = uiSettings,
        ) {
            // Parking location marker
            parkingMarkerState?.let {
                Marker(
                    state = parkingMarkerState,
                    title = "Parked Here",
                    snippet = "Your car's location"
                )
            }
        }
    }
}