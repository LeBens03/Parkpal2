package com.example.parkpal.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "car_table",
    foreignKeys = [ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["userId"],
        childColumns = ["userId"],
        onDelete = ForeignKey.Companion.CASCADE
    ),
        ForeignKey(
            entity = ParkingLocationEntity::class,
            parentColumns = ["parkingLocationId"],
            childColumns = ["currentParkingLocationId"],
            onDelete = ForeignKey.Companion.SET_NULL
        )
    ],
    indices = [Index("userId"), Index("currentParkingLocationId")]
)
data class CarEntity(
    @PrimaryKey(autoGenerate = true)
    val carId: Long = 0,
    val userId: Long = 0,
    val brand: String,
    val model: String,
    val year: Int,
    val licensePlate: String,
    val currentParkingLocationId: Int? = null
)