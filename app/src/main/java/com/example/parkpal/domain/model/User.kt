package com.example.parkpal.domain.model

import androidx.room.PrimaryKey

data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Int = 0,
    val name: String,
    val email: String,
    val password: String?,
    val phoneNumber: String?,
    val city: String,
    val birthDate: String
)
