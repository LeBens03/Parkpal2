package com.example.parkpal.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "parking_location_table",
    foreignKeys = [ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["userId"],
        childColumns = ["userId"],
        onDelete = ForeignKey.Companion.CASCADE
    ),
        ForeignKey(
            entity = CarEntity::class,
            parentColumns = ["carId"],
            childColumns = ["carId"],
            onDelete = ForeignKey.Companion.CASCADE
        )
    ],
    indices = [Index("userId"), Index("carId")]
)
data class ParkingLocationEntity(
    @PrimaryKey(autoGenerate = true)
    val parkingLocationId: Long = 0,
    val userId: Long,
    val carId: Long,
    val latitude: Double,
    val longitude: Double,
    val timestamp: String
)