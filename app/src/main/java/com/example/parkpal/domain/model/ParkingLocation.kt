package com.example.parkpal.domain.model

data class ParkingLocation(
    val parkingLocationId: Int = 0,
    val userId: Int, // Foreign key to User table
    val carId: Int, // Foreign key to Car table
    val latitude: Double,
    val longitude: Double,
    val timestamp: String
)