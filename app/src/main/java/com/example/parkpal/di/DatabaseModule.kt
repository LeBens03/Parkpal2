package com.example.parkpal.di

import android.content.Context
import androidx.room.Room
import com.example.parkpal.data.AppDatabase
import com.example.parkpal.data.dao.CarDao
import com.example.parkpal.data.dao.ParkingLocationDao
import com.example.parkpal.data.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "parkpal_database"
        )
            .build()
    }

    @Provides
    fun provideUserDao(database: AppDatabase) : UserDao = database.userDao()

    @Provides
    fun provideCarDao(database: AppDatabase) : CarDao = database.carDao()

    @Provides
    fun provideParkingLocationDao(database: AppDatabase) : ParkingLocationDao = database.parkingLocationDao()
}