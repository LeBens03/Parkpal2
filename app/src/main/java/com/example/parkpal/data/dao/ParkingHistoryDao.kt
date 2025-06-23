package com.example.parkpal.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.parkpal.data.entity.ParkingHistoryEntity

@Dao
interface ParkingHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParkingHistory(parkingHistory: ParkingHistoryEntity): Long

    @Delete
    suspend fun deleteParkingHistory(parkingHistory: ParkingHistoryEntity)

    @Query(
        "SELECT * FROM parking_history_table " +
        "WHERE userId = :userId ")
    suspend fun getParkingHistoryByUserId(userId: Long): ParkingHistoryEntity

    @Query(
        "SELECT * FROM parking_history_table " +
                "WHERE carId = :carId ")
    suspend fun getParkingHistoryByCarId(carId: Long): ParkingHistoryEntity
}