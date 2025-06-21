package com.example.parkpal.di

import android.content.Context
import com.example.parkpal.data.dao.CarDao
import com.example.parkpal.data.dao.ParkingLocationDao
import com.example.parkpal.data.dao.UserDao
import com.example.parkpal.domain.repository.CarRepository
import com.example.parkpal.domain.repository.ParkingLocationRepository
import com.example.parkpal.domain.repository.UserRepository
import com.example.parkpal.presentation.UserLocationImpl
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(
        @ApplicationContext context: Context
    ): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    @Provides
    @Singleton
    fun provideUserLocationImpl(
        @ApplicationContext context: Context,
        fusedLocationClient: FusedLocationProviderClient
    ): UserLocationImpl {
        return UserLocationImpl(context, fusedLocationClient)
    }
}