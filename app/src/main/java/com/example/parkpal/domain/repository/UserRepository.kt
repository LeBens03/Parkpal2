package com.example.parkpal.domain.repository

import android.util.Log
import com.example.parkpal.data.dao.UserDao
import com.example.parkpal.data.mapper.toUser
import com.example.parkpal.data.mapper.toUserEntity
import com.example.parkpal.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private val userDao: UserDao) {

    suspend fun insertUser(user: User) {
        Log.d("UserRepository", "Insert user: $user")
        userDao.insertUser(user.toUserEntity())
    }

    suspend fun updateUser(user: User) {
        Log.d("UserRepository", "Update user: $user")
        userDao.updateUser(user.toUserEntity())
    }

    suspend fun deleteUser(user: User) {
        Log.d("UserRepository", "Delete user: $user")
        userDao.deleteUser(user.toUserEntity())
    }

    fun getAllUsers() : Flow<List<User>> {
        Log.d("UserRepository", "Get all users")
        return userDao.getAllUsers().map { userEntities ->
            userEntities.map { it.toUser() }
        }
    }
}