package com.example.parkpal.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.parkpal.data.entity.CarEntity
import com.example.parkpal.data.entity.ParkingLocationEntity

@Dao
interface CarDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertCar(car: CarEntity): Long

    @Update
    suspend fun updateCar(car: CarEntity)

    @Delete
    suspend fun deleteCar(car: CarEntity)

    @Query("SELECT * FROM car_table WHERE carId = :userId")
    suspend fun getCarByUserId(userId: Long): List<CarEntity>

    @Transaction
    @Query("SELECT * FROM parking_location_table WHERE carId = :carId ")
    suspend fun getParkingLocation(carId: Long): ParkingLocationEntity
}