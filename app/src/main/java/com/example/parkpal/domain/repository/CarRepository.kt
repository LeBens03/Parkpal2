package com.example.parkpal.domain.repository

import android.util.Log
import com.example.parkpal.data.dao.CarDao
import com.example.parkpal.data.mapper.toCar
import com.example.parkpal.data.mapper.toCarEntity
import com.example.parkpal.domain.model.Car
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CarRepository @Inject constructor(private val carDao: CarDao) {

    suspend fun insertCar(car: Car) : Long {
        Log.d("CarRepository", "Insert car: $car")
        return carDao.insertCar(car.toCarEntity())
    }

    suspend fun deleteCar(car: Car) {
        Log.d("CarRepository", "Delete car: $car")
        carDao.deleteCar(car.toCarEntity())
    }

    suspend fun getCarsByUserId(userId: Long): List<Car> {
        Log.d("CarRepository", "Get cars by userId: $userId")
        return carDao.getCarsByUserId(userId).map{ it.toCar() }
    }

    suspend fun getCarByUserEmail(userEmail: String): List<Car> {
        Log.d("CarRepository", "Get cars by userEmail: $userEmail")
        return carDao.getCarsByUserEmail(userEmail).map { it.toCar() }
    }

    suspend fun getCarById(carId: Long): Car? {
        Log.d("CarRepository", "Get car by id: $carId")
        return carDao.getCarById(carId)?.toCar()
    }

}