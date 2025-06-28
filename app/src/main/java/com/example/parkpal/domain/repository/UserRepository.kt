package com.example.parkpal.domain.repository

import android.util.Log
import com.example.parkpal.data.dao.UserDao
import com.example.parkpal.data.mapper.toUser
import com.example.parkpal.data.mapper.toUserEntity
import com.example.parkpal.domain.model.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private val userDao: UserDao) {

    suspend fun insertUser(user: User) : Long {
        Log.d("UserRepository", "Insert user: $user")
        return userDao.insertUser(user.toUserEntity())
    }

    suspend fun updateUser(user: User) {
        Log.d("UserRepository", "Update user: $user")
        userDao.updateUser(user.toUserEntity())
    }

    suspend fun getUserByEmail(email: String) : User? {
        Log.d("UserRepository", "Get user by email: $email")
        return userDao.getUserByEmail(email)?.toUser()
    }

}