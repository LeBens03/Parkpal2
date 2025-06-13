package com.example.parkpal.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.parkpal.data.dao.CarDao
import com.example.parkpal.data.dao.ParkingLocationDao
import com.example.parkpal.data.dao.UserDao
import com.example.parkpal.data.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun carDao(): CarDao
    abstract fun parkingLocationDao(): ParkingLocationDao
}