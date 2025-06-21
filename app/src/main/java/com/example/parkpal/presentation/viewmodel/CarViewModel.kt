package com.example.parkpal.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkpal.domain.model.Car
import com.example.parkpal.domain.model.User
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
                _currentCar.value = null
            } catch (e: Exception) {
                _errorMessage.emit("Failed to delete car: ${e.localizedMessage}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getCarById(carId: Int) {
        viewModelScope.launch {
            carRepository.getCarById(carId)
        }
    }

    fun getParkingLocation(carId: Int) {
        viewModelScope.launch {
            carRepository.getParkingLocation(carId)
        }
    }

}