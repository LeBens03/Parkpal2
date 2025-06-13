package com.example.parkpal.domain.repository

import android.util.Log
import com.example.parkpal.data.dao.CarDao
import com.example.parkpal.data.mapper.toCar
import com.example.parkpal.data.mapper.toCarEntity
import com.example.parkpal.data.mapper.toParkingLocation
import com.example.parkpal.domain.model.Car
import com.example.parkpal.domain.model.ParkingLocation
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CarRepository @Inject constructor(private val carDao: CarDao) {

    suspend fun insertCar(car: Car) {
        Log.d("CarRepository", "Insert car: $car")
        carDao.insertCar(car.toCarEntity())
    }

    suspend fun updateCar(car: Car) {
        Log.d("CarRepository", "Update car: $car")
        carDao.updateCar(car.toCarEntity())
    }

    suspend fun deleteCar(car: Car) {
        Log.d("CarRepository", "Delete car: $car")
        carDao.deleteCar(car.toCarEntity())
    }

    suspend fun getCarById(carId: Int): Car {
        Log.d("CarRepository", "Get car by id: $carId")
        return carDao.getCarById(carId).toCar()
    }

    suspend fun getParkingLocation(carId: Int): ParkingLocation {
        Log.d("CarRepository", "Get parking location for car: $carId")
        return carDao.getParkingLocation(carId).toParkingLocation()
    }

}