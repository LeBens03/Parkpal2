package com.example.parkpal.domain.model

data class ParkingHistory(
    val userId: Long,
    val carId: Long,
    val parkingLocations: List<ParkingLocation>
)