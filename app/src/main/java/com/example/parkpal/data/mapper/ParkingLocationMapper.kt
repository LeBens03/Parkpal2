package com.example.parkpal.data.mapper

import com.example.parkpal.data.entity.ParkingLocationEntity
import com.example.parkpal.domain.model.ParkingLocation

fun ParkingLocationEntity.toParkingLocation(): ParkingLocation {
    return ParkingLocation(
        parkingLocationId = parkingLocationId,
        userId = userId,
        carId = carId,
        latitude = latitude,
        longitude = longitude,
        timestamp = timestamp
    )
}

fun ParkingLocation.toParkingLocationEntity(): ParkingLocationEntity {
    return ParkingLocationEntity(
        parkingLocationId = parkingLocationId,
        userId = userId,
        carId = carId,
        latitude = latitude,
        longitude = longitude,
        timestamp = timestamp
    )
}