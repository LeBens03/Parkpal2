package com.example.parkpal.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.parkpal.data.dao.CarDao
import com.example.parkpal.data.dao.ParkingLocationDao
import com.example.parkpal.data.dao.UserDao
import com.example.parkpal.data.entity.CarEntity
import com.example.parkpal.data.entity.ParkingLocationEntity
import com.example.parkpal.data.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        CarEntity::class,
        ParkingLocationEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun carDao(): CarDao
    abstract fun parkingLocationDao(): ParkingLocationDao
}