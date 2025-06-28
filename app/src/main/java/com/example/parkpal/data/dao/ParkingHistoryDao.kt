package com.example.parkpal.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.parkpal.data.entity.ParkingHistoryEntity

@Dao
interface ParkingHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParkingHistory(parkingHistory: ParkingHistoryEntity): Long

    @Query("DELETE FROM parking_history_table WHERE userId = :historyId")
    suspend fun deleteParkingHistoryById(historyId: Long)

    @Query(
        "SELECT * FROM parking_history_table " +
        "WHERE userId = :userId " +
        "ORDER BY parkingHistoryId DESC LIMIT 1")
    suspend fun getParkingHistoryByUserId(userId: Long): ParkingHistoryEntity?
}