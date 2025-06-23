package com.example.parkpal.presentation.viewmodel

import android.content.Context
import android.location.Geocoder
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.parkpal.presentation.MapState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.parkpal.R
import com.example.parkpal.domain.model.ParkingLocation
import com.example.parkpal.domain.repository.ParkingLocationRepository
import com.example.parkpal.presentation.MapEvent
import com.example.parkpal.presentation.UserLocationImpl
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val parkingLocationRepository: ParkingLocationRepository,
    private val userLocationImpl: UserLocationImpl
) : ViewModel() {

    private var _state by mutableStateOf(MapState())
    val state: MapState get() = _state

    private val _address = MutableStateFlow("Loading...")
    val address: StateFlow<String> get() = _address

    private val _distance = MutableStateFlow<Float?>(null)
    val distance: StateFlow<Float?> get() = _distance

    init {
        loadMapStyle()
    }

    private fun updateState(update: MapState.() -> MapState) {
        _state = _state.update()
    }

    private fun loadMapStyle() {
        try {
            val mapStyle = MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style)
            updateState {
                copy(
                    properties = properties.copy(
                        mapStyleOptions = mapStyle
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun startLocationUpdates() {
        viewModelScope.launch {
            userLocationImpl.getLocationUpdates(interval = 5000L) // Update every 5 seconds
                .collect { location ->
                    updateState {
                        copy(userLocation = LatLng(location.latitude, location.longitude))
                    }
                }
        }
    }

    fun fetchAddress(context: Context, parkingLocation: ParkingLocation?) {
        viewModelScope.launch {
            val resolvedAddress = withContext(Dispatchers.IO) {
                if (parkingLocation == null) {
                    return@withContext "Unknown Address"
                }

                try {
                    val geocoder = Geocoder(context, Locale.getDefault())
                    val addressList = geocoder.getFromLocation(parkingLocation.latitude, parkingLocation.longitude, 1)
                    addressList?.firstOrNull()?.getAddressLine(0) ?: "Unknown Address"
                } catch (e: Exception) {
                    "Unknown Address"
                }
            }
            updateState {
                copy(
                    parkingLocation = parkingLocation?.copy(address = resolvedAddress)
                )
            }
            _address.value = resolvedAddress
        }
    }

    fun calculateDistance(userLocation: LatLng?, parkingLocation: ParkingLocation?) {
        viewModelScope.launch {
            val calculatedDistance = if (userLocation == null || parkingLocation == null) {
                null
            } else {
                val results = FloatArray(1)
                android.location.Location.distanceBetween(
                    userLocation.latitude, userLocation.longitude,
                    parkingLocation.latitude, parkingLocation.longitude,
                    results
                )
                results[0] / 1000 // Convert to kilometers
            }
            _distance.value = calculatedDistance
        }
    }

    fun onEvent(event: MapEvent) {
        when (event) {
            is MapEvent.OnParkMyCarClicked -> {
                event.parkingLocation.let { location ->
                    viewModelScope.launch {
                        try {
                            Log.d("MapViewModel", "Inserting parking location: $location")
                            parkingLocationRepository.insertParkingLocation(location)
                            updateState { copy(parkingLocation = location) }
                        } catch (e: Exception) {
                            println("Error inserting parking location: ${e.message}")
                        }
                    }
                }
            }

            is MapEvent.OnParkingLocationClicked -> {
                event.parkingLocation.let { location ->
                    viewModelScope.launch {
                        try {
                            Log.d("MapViewModel", "Updating parking location: $location")
                            parkingLocationRepository.insertParkingLocation(location)
                            updateState { copy(parkingLocation = null) }
                        } catch (e: Exception) {
                            println("Error updating parking location: ${e.message}")
                        }
                    }
                }
            }
        }
    }
}