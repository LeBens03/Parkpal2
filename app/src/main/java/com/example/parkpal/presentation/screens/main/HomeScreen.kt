package com.example.parkpal.presentation.screens.main

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.core.net.toUri
import com.example.parkpal.presentation.BottomSheetContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    mapViewModel: MapViewModel = hiltViewModel(),
    carViewModel: CarViewModel
) {
    val state = mapViewModel.state
    val uiSettings = remember { MapUiSettings(
        zoomControlsEnabled = false,
        zoomGesturesEnabled = true,
        compassEnabled = true,
        myLocationButtonEnabled = true
    ) }

    val parkingMarkerState = state.parkingLocation?.let { rememberMarkerState(position = LatLng(it.latitude, it.longitude)) }

    val defaultCityLatLng = LatLng(45.0703, 7.6869)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultCityLatLng, 12f) //
    }

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    val currentCar by carViewModel.currentCar.observeAsState()

    var permissionsGranted by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val requestPermissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            permissionsGranted = permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                    permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true
        }
    )

    val address = state.parkingLocation?.let { parkingLoc ->
        Geocoder(context).getFromLocation(parkingLoc.latitude, parkingLoc.longitude, 1)
            ?.firstOrNull()?.getAddressLine(0) ?: "Unknown Address"
    }

    val distance = state.userLocation?.let { userLoc ->
        state.parkingLocation?.let { parkingLoc ->
            val results = FloatArray(1)
            android.location.Location.distanceBetween(
                userLoc.latitude, userLoc.longitude,
                parkingLoc.latitude, parkingLoc.longitude,
                results
            )
            results[0] / 1000 // Distance in kilometers
        }
    }

    Log.d("HomeScreen", "Current car: $currentCar")
    Log.d("HomeScreen", "User location: ${state.userLocation}")
    Log.d("HomeScreen", "Parking location: ${state.parkingLocation}")
    Log.d("HomeScreen", "Address: $address")
    Log.d("HomeScreen", "Distance: $distance")

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
                    snippet = "Your car's location",
                    onClick = {
                        showBottomSheet = true
                        true
                    }
                )
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            BottomSheetContent(
                distance = distance,
                address = address,
                onNavigateClick = {
                    val uri =
                        "google.navigation:q=${state.parkingLocation?.latitude},${state.parkingLocation?.longitude}".toUri()
                    val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                        setPackage("com.google.android.apps.maps")
                    }
                    context.startActivity(intent)
                },
                onArrivedClick = {
                    mapViewModel.onEvent(MapEvent.OnParkingLocationClicked(state.parkingLocation!!))
                    showBottomSheet = false
                },
            )
        }
    }
}