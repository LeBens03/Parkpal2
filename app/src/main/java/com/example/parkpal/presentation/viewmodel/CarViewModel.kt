package com.example.parkpal.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkpal.domain.model.Car
import com.example.parkpal.domain.repository.CarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarViewModel @Inject constructor(
    private val carRepository: CarRepository
) : ViewModel() {

    fun insertCar(car: Car) {
        viewModelScope.launch {
            carRepository.insertCar(car)
        }
    }

    fun updateCar(car: Car) {
        viewModelScope.launch {
            carRepository.updateCar(car)
        }
    }

    fun deleteCar(car: Car) {
        viewModelScope.launch {
            carRepository.deleteCar(car)
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