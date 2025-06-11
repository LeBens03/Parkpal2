package com.example.parkpal.domain.repository

import android.util.Log
import com.example.parkpal.data.dao.UserDao
import com.example.parkpal.data.mapper.toUserEntity
import com.example.parkpal.domain.model.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private val userDao: UserDao) {

    suspend fun insertUser(user: User) {
        Log.d("UserRepository", "Insert user: $user")
        userDao.insertUser(user.toUserEntity())
    }

    fun getAllUsers() = userDao.getAllUsers()
}