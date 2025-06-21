package com.example.parkpal.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.parkpal.presentation.MapState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.parkpal.R
import com.example.parkpal.domain.repository.ParkingLocationRepository
import com.example.parkpal.presentation.MapEvent
import com.example.parkpal.presentation.UserLocationImpl
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val parkingLocationRepository: ParkingLocationRepository,
    private val userLocationImpl: UserLocationImpl
) : ViewModel() {

    private var _state by mutableStateOf(MapState())
    val state: MapState get() = _state

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
                            parkingLocationRepository.deleteParkingLocation(location)
                            updateState { copy(parkingLocation = null) }
                        } catch (e: Exception) {
                            println("Error deleting parking location: ${e.message}")
                        }
                    }
                }
            }
        }
    }
}