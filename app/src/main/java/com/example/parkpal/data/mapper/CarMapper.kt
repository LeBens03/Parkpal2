package com.example.parkpal.data.mapper

import com.example.parkpal.data.entity.CarEntity
import com.example.parkpal.domain.model.Car

fun CarEntity.toCar(): Car {
    return Car(
        carId = carId,
        userId = userId,
        brand = brand,
        model = model,
        year = year,
        licensePlate = licensePlate,
        currentParkingLocationId = currentParkingLocationId
    )
}

fun Car.toCarEntity(): CarEntity {
    return CarEntity(
        carId = carId,
        userId = userId,
        brand = brand,
        model = model,
        year = year,
        licensePlate = licensePlate,
        currentParkingLocationId = currentParkingLocationId
    )
}