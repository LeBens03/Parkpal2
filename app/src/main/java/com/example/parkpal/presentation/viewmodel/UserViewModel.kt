package com.example.parkpal.presentation.viewmodel

import android.util.Log
import android.util.Log.e
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkpal.domain.model.User
import com.example.parkpal.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    val users = userRepository.getAllUsers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _userId = MutableStateFlow<Long?>(null)
    val userId: StateFlow<Long?> = _userId.asStateFlow()

    fun insertUser(user: User) {
        Log.d("UserViewModel", "insertUser called")
        viewModelScope.launch {
            try {
                Log.d("UserViewModel", "Coroutine started")
                _userId.value = userRepository.insertUser(user)
                Log.d("UserViewModel", "User inserted with id ${_userId.value}")
            } catch (e: Exception) {
                Log.e("UserViewModel", "Error inserting user", e)
            }
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            userRepository.updateUser(user)
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            userRepository.deleteUser(user)
        }
    }
}