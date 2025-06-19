package com.example.parkpal.presentation.viewmodel

import android.util.Log
import android.util.Log.e
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkpal.domain.model.User
import com.example.parkpal.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _currentUser = MutableLiveData<User?>()
    val currentUser: MutableLiveData<User?> = _currentUser

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableSharedFlow<String>() // one-off events
    val errorMessage: SharedFlow<String> = _errorMessage.asSharedFlow()

    fun insertUser(user: User) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val userId = userRepository.insertUser(user)
                currentUser.value = user.copy(userId = userId)
            } catch (e: Exception) {
                _errorMessage.emit("Failed to insert user: ${e.localizedMessage}")
            } finally {
                _isLoading.value = false
                Log.d("UserViewModel", "User inserted with id ${currentUser.value?.userId}")
            }
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                userRepository.updateUser(user)
                currentUser.value = user
            } catch (e: Exception) {
                _errorMessage.emit("Failed to update user: ${e.localizedMessage}")
            } finally {
                _isLoading.value = false
                Log.d("UserViewModel", "User updated with id ${currentUser.value?.userId}")
            }
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                userRepository.deleteUser(user)
                currentUser.value = null
            } catch (e: Exception) {
                _errorMessage.emit("Failed to delete user: ${e.localizedMessage}")
            } finally {
                _isLoading.value = false
                Log.d("UserViewModel", "User deleted with id ${currentUser.value?.userId}")
            }
        }
    }
}