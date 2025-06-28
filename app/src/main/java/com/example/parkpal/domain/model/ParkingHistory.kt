package com.example.parkpal.domain.model

data class ParkingHistory(
    val parkingHistoryId: Long = 0,
    val userId: Long,
    val parkingLocations: List<ParkingLocation>
)