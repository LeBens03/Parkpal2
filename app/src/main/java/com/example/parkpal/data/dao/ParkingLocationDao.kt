package com.example.parkpal.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.parkpal.data.entity.ParkingLocationEntity

@Dao
interface ParkingLocationDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertParkingLocation(parkingLocation: ParkingLocationEntity): Long

    @Delete
    suspend fun deleteParkingLocation(parkingLocation: ParkingLocationEntity)
}