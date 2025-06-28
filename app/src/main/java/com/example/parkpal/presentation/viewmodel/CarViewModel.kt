package com.example.parkpal.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkpal.domain.model.Car
import com.example.parkpal.domain.repository.CarRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarViewModel @Inject constructor(
    private val carRepository: CarRepository
) : ViewModel() {

    private val _currentUserCars = MutableStateFlow<List<Car>>(emptyList())
    val currentUserCars: StateFlow<List<Car>> = _currentUserCars

    private val _licensePlates = MutableStateFlow<Map<Long, String>>(emptyMap())
    val licensePlates: StateFlow<Map<Long, String>> = _licensePlates

    fun fetchCarsOfCurrentUser() {
        viewModelScope.launch {
            try {
                val email = FirebaseAuth.getInstance().currentUser?.email
                    ?: throw IllegalStateException("User is not authenticated")

                Log.d("CarViewModel", "Current user email: $email")

                val cars = carRepository.getCarByUserEmail(email)
                _currentUserCars.value = cars

                Log.d("CarViewModel", "Successfully fetched ${cars.size} cars for user: $email")

            } catch (e: Exception) {
                Log.e("CarViewModel", "Failed to fetch cars of current user", e)
            }
        }
    }

    fun insertCar(car: Car) {
        viewModelScope.launch {
            try {
                carRepository.insertCar(car)
                _currentUserCars.value = _currentUserCars.value + car
                Log.d("CarViewModel", "Car inserted successfully")
            } catch (e: Exception) {
                Log.e("CarViewModel", "Failed to insert car", e)
            }
        }
    }

    fun deleteCar(car: Car) {
        viewModelScope.launch {
            try {
                carRepository.deleteCar(car)
                _currentUserCars.value = _currentUserCars.value.filter { it.carId != car.carId }
                Log.d("CarViewModel", "Car deleted successfully")
            } catch (e: Exception) {
                Log.e("CarViewModel", "Failed to delete car", e)
            }
        }
    }

    fun getCarByUserId(userId: Long) {
        viewModelScope.launch {
            val list = carRepository.getCarsByUserId(userId)
            _currentUserCars.value = list
        }
    }

    fun fetchLicensePlatesForCars(carIds: List<Long>) {
        viewModelScope.launch {
            try {
                val plates = carIds.associateWith { carId ->
                    carRepository.getCarById(carId)?.licensePlate ?: "Unknown"
                }
                _licensePlates.value = plates
                Log.d("CarViewModel", "Loaded license plates: $plates")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun clearCarOfCurrentUser() {
        _currentUserCars.value = emptyList()
        Log.d("CarViewModel", "Cleared current cars")
    }

}