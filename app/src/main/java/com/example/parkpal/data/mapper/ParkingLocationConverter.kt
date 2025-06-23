package com.example.parkpal.data.mapper

import androidx.room.TypeConverter
import com.example.parkpal.domain.model.ParkingLocation
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ParkingLocationConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromParkingLocationList(value: List<ParkingLocation>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toParkingLocationList(value: String): List<ParkingLocation> {
        val type = object : TypeToken<List<ParkingLocation>>() {}.type
        return gson.fromJson(value, type)
    }
}
