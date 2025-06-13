package com.example.parkpal.di

import com.example.parkpal.data.dao.CarDao
import com.example.parkpal.data.dao.ParkingLocationDao
import com.example.parkpal.data.dao.UserDao
import com.example.parkpal.domain.repository.CarRepository
import com.example.parkpal.domain.repository.ParkingLocationRepository
import com.example.parkpal.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideUserRepository(userDao: UserDao): UserRepository {
        return UserRepository(userDao)
    }

    @Singleton
    @Provides
    fun provideCarRepository(carDao: CarDao): CarRepository {
        return CarRepository(carDao)
    }

    @Singleton
    @Provides
    fun provideParkingLocationRepository(parkingLocationDao: ParkingLocationDao) : ParkingLocationRepository {
        return ParkingLocationRepository(parkingLocationDao)
    }
}