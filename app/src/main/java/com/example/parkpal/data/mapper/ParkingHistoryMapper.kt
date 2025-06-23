package com.example.parkpal.data.mapper

import com.example.parkpal.domain.model.ParkingHistory
import com.example.parkpal.data.entity.ParkingHistoryEntity


fun ParkingHistoryEntity.toParkingHistory(): ParkingHistory{
    return ParkingHistory(
        userId = userId,
        carId = carId,
        parkingLocations = parkingLocations
    )
}

fun ParkingHistory.toParkingHistoryEntity(): ParkingHistoryEntity{
    return ParkingHistoryEntity(
        userId = userId,
        carId = carId,
        parkingLocations = parkingLocations
    )
}