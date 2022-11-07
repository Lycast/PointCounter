package com.example.pointcounter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pointcounter.model.entity.User
import com.example.pointcounter.repository.Repository
import kotlinx.coroutines.launch

class SharedViewModel (private val repository: Repository) : ViewModel() {

    val users = repository.listUsers

    fun addUser(user: User) = viewModelScope.launch {
        repository.addUser(user)
    }

    fun updateUser(user: User) = viewModelScope.launch {
        repository.updateUser(user)
    }

    fun deleteUser(user: User) = viewModelScope.launch {
        repository.deleteUser(user)
    }

    fun deleteAllUsers() = viewModelScope.launch {
        repository.deleteAllUsers()
    }
}
