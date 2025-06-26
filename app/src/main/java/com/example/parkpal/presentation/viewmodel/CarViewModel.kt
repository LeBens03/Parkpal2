package com.example.parkpal.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkpal.domain.model.Car
import com.example.parkpal.domain.repository.CarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarViewModel @Inject constructor(
    private val carRepository: CarRepository
) : ViewModel() {

    private val _currentCar = MutableLiveData<Car?>()
    val currentCar: MutableLiveData<Car?> = _currentCar

    private val _currentUserCars = MutableStateFlow<List<Car>>(emptyList())
    val currentUserCars: StateFlow<List<Car>> = _currentUserCars

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage: SharedFlow<String> = _errorMessage.asSharedFlow()

    fun insertCar(car: Car) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val carId = carRepository.insertCar(car)
                _currentCar.value = car.copy(carId = carId)
                _currentUserCars.value = _currentUserCars.value + car
                Log.d("CarViewModel", "Car inserted successfully")
            } catch (e: Exception) {
                _errorMessage.emit("Failed to insert car: ${e.localizedMessage}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateCar(car: Car) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                carRepository.updateCar(car)
                _currentCar.value = car
            } catch (e: Exception) {
                _errorMessage.emit("Failed to update car: ${e.localizedMessage}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteCar(car: Car) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                carRepository.deleteCar(car)
                _currentUserCars.value = _currentUserCars.value.filter { it.carId != car.carId }
                _currentCar.value = null
                Log.d("CarViewModel", "Car deleted successfully")
            } catch (e: Exception) {
                _errorMessage.emit("Failed to delete car: ${e.localizedMessage}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getCarByUserId(userId: Long) {
        viewModelScope.launch {
            val list = carRepository.getCarByUserId(userId)
            _currentUserCars.value = list
        }
    }

    fun getParkingLocation(carId: Long) {
        viewModelScope.launch {
            carRepository.getParkingLocation(carId)
        }
    }

}