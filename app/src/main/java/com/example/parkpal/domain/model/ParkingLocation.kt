package com.example.parkpal.domain.model

data class ParkingLocation(
    val parkingLocationId: Long = 0,
    val userId: Long,
    val carId: Long,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val timestamp: Long,
    val duration: Long
    )