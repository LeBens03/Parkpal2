package com.example.parkpal.domain.model

data class ParkingLocation(
    val parkingLocationId: Long = 0,
    val userId: Long, // Foreign key to User table
    val carId: Long, // Foreign key to Car table
    val latitude: Double,
    val longitude: Double,
    val timestamp: String
)