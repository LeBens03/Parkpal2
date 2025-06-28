package com.example.parkpal.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.parkpal.data.mapper.ParkingLocationConverter
import com.example.parkpal.domain.model.ParkingLocation

@Entity(
    tableName = "parking_history_table",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userId")]
)
@TypeConverters(ParkingLocationConverter::class)
data class ParkingHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val parkingHistoryId: Long = 0,
    val userId: Long,
    val parkingLocations: List<ParkingLocation>
)