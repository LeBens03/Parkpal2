package com.example.parkpal.domain.model

data class User(
    val userId: Long = 0,
    val name: String,
    val email: String,
    val password: String?,
    val phoneNumber: String?,
    val gender: String?,
    val address: String?,
    val city: String,
    val country: String?,
    val zipCode: String?,
    val birthDate: String
)
