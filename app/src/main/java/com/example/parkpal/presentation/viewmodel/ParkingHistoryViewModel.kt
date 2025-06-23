package com.example.parkpal.presentation.viewmodel

import android.util.Log
import com.example.parkpal.domain.model.ParkingHistory
import com.example.parkpal.domain.repository.ParkingHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import com.example.parkpal.domain.model.ParkingLocation

@HiltViewModel
class ParkingHistoryViewModel @Inject constructor(
    private val parkingHistoryRepository: ParkingHistoryRepository
) : ViewModel() {

    private val _currentParkingHistory = MutableStateFlow<ParkingHistory?>(null)
    val currentParkingHistory: StateFlow<ParkingHistory?> get() = _currentParkingHistory

    fun fetchParkingHistory(userId: Long) {
        viewModelScope.launch {
            try {
                val history = parkingHistoryRepository.getParkingHistoryByUserId(userId)
                _currentParkingHistory.value = history
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addParkingLocation(parkingLocation: ParkingLocation, carId: Long, userId: Long) {
        viewModelScope.launch {
            try {
                Log.d("ParkingHistoryViewModel", "Adding parking location to parking history: $parkingLocation")

                val currentHistory = _currentParkingHistory.value
                val updatedParkingHistory = if (currentHistory != null) {
                    currentHistory.copy(
                        parkingLocations = currentHistory.parkingLocations + parkingLocation
                    )
                } else {
                    ParkingHistory(
                        userId = userId,
                        carId = carId,
                        parkingLocations = listOf(parkingLocation)
                    )
                }

                parkingHistoryRepository.insertParkingHistory(updatedParkingHistory)

                _currentParkingHistory.value = updatedParkingHistory

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteParkingLocation(parkingLocation: ParkingLocation) {
        viewModelScope.launch {
            try {
                Log.d("ParkingHistoryViewModel", "Deleting parking location: $parkingLocation")
                val currentHistory = _currentParkingHistory.value
                if (currentHistory != null) {
                    val updatedParkingHistory = currentHistory.copy(
                        parkingLocations = currentHistory.parkingLocations - parkingLocation
                    )
                    parkingHistoryRepository.insertParkingHistory(updatedParkingHistory)
                    _currentParkingHistory.value = updatedParkingHistory
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun clearParkingHistory() {
        viewModelScope.launch {
            try {
                val currentHistory = currentParkingHistory.value
                Log.d("ParkingHistoryViewModel", "Deleting parking history: $currentHistory")
                if (currentHistory != null) {
                    parkingHistoryRepository.deleteParkingHistory(currentHistory)
                    _currentParkingHistory.value = null
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}