package com.example.parkpal.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.TypeConverters
import com.example.parkpal.data.mapper.ParkingLocationConverter
import com.example.parkpal.domain.model.ParkingLocation

@Entity(
    tableName = "parking_history_table",
    primaryKeys = ["userId", "carId"],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CarEntity::class,
            parentColumns = ["carId"],
            childColumns = ["carId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userId"), Index("carId")]
)
@TypeConverters(ParkingLocationConverter::class)
data class ParkingHistoryEntity(
    val userId: Long,
    val carId: Long,
    val parkingLocations: List<ParkingLocation>
)