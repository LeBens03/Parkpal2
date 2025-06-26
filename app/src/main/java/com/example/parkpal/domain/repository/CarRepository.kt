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

    suspend fun insertCar(car: Car) : Long {
        Log.d("CarRepository", "Insert car: $car")
        return carDao.insertCar(car.toCarEntity())
    }

    suspend fun updateCar(car: Car) {
        Log.d("CarRepository", "Update car: $car")
        carDao.updateCar(car.toCarEntity())
    }

    suspend fun deleteCar(car: Car) {
        Log.d("CarRepository", "Delete car: $car")
        carDao.deleteCar(car.toCarEntity())
    }

    suspend fun getCarByUserId(userId: Long): List<Car> {
        Log.d("CarRepository", "Get car by userId: $userId")
        return carDao.getCarByUserId(userId).map { it.toCar() }
    }

    suspend fun getParkingLocation(carId: Long): ParkingLocation {
        Log.d("CarRepository", "Get parking location for car: $carId")
        return carDao.getParkingLocation(carId).toParkingLocation()
    }

}