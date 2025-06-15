package com.example.parkpal.domain.model

data class User(
    val userId: Long = 0,
    val name: String,
    val email: String,
    val password: String?,
    val phoneNumber: String?,
    val city: String,
    val birthDate: String
)
