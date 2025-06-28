package com.example.parkpal.domain.repository

import android.util.Log
import com.example.parkpal.data.dao.ParkingHistoryDao
import com.example.parkpal.data.mapper.toParkingHistory
import com.example.parkpal.data.mapper.toParkingHistoryEntity
import com.example.parkpal.domain.model.ParkingHistory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ParkingHistoryRepository @Inject constructor(
    private val parkingHistoryDao: ParkingHistoryDao
) {
    suspend fun insertParkingHistory(parkingHistory: ParkingHistory) {
        Log.d("ParkingHistoryRepository", "Insert parking history: $parkingHistory")
        parkingHistoryDao.insertParkingHistory(parkingHistory.toParkingHistoryEntity())
    }

    suspend fun deleteParkingHistoryById(historyId: Long) {
        Log.d("ParkingHistoryRepository", "Delete parking history by ID: $historyId")
        parkingHistoryDao.deleteParkingHistoryById(historyId)
    }

    suspend fun getParkingHistoryByUserId(userId: Long): ParkingHistory? {
        Log.d("ParkingHistoryRepository", "Get parking history by user ID: $userId")
        return parkingHistoryDao.getParkingHistoryByUserId(userId)?.toParkingHistory()
    }
}