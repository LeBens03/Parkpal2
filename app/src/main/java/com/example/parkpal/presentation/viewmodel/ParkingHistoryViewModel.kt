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

    fun deleteParkingHistory(parkingHistory: ParkingHistory) {
        viewModelScope.launch {
            try {
                Log.d("ParkingHistoryViewModel", "Deleting parking history: $parkingHistory")
                parkingHistoryRepository.deleteParkingHistory(parkingHistory)
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
}