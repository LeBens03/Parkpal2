package com.example.parkpal.domain.repository

import android.util.Log
import com.example.parkpal.data.dao.ParkingLocationDao
import com.example.parkpal.data.mapper.toParkingLocationEntity
import com.example.parkpal.domain.model.ParkingLocation
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ParkingLocationRepository @Inject constructor(
    private val parkingLocationDao: ParkingLocationDao
) {
    suspend fun insertParkingLocation(parkingLocation: ParkingLocation) {
        Log.d("ParkingLocationRepository", "Insert parking location: $parkingLocation")
        parkingLocationDao.insertParkingLocation(parkingLocation.toParkingLocationEntity())
    }

    suspend fun deleteParkingLocation(parkingLocation: ParkingLocation) {
        Log.d("ParkingLocationRepository", "Delete parking location: $parkingLocation")
        parkingLocationDao.deleteParkingLocation(parkingLocation.toParkingLocationEntity())
    }
}