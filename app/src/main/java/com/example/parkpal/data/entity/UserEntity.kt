package com.example.parkpal.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val userId: Long,
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