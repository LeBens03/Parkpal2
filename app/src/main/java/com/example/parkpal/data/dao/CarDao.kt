package com.example.parkpal.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
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

    @Query("SELECT * FROM car_table WHERE userId = :userId")
    suspend fun getCarsByUserId(userId: Long): List<CarEntity>

    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM car_table, user_table WHERE car_table.userId = user_table.userId AND user_table.email = :userEmail")
    suspend fun getCarsByUserEmail(userEmail: String): List<CarEntity>

    @Query("SELECT * FROM car_table WHERE carId = :carId")
    suspend fun getCarById(carId: Long): CarEntity?

    @Transaction
    @Query("SELECT * FROM parking_location_table WHERE carId = :carId ")
    suspend fun getParkingLocation(carId: Long): ParkingLocationEntity
}