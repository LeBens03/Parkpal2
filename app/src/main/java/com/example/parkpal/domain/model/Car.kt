package com.example.parkpal.domain.model

data class Car(
    val carId: Int = 0,
    val userId: Int, // Foreign key to User table
    val brand: String,
    val model: String,
    val year: Int,
    val licensePlate: String,
    val currentParkingLocationId: Int? = null // Foreign key to the current parking location
)