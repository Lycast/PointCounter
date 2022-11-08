package com.example.pointcounter.viewmodel

import android.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pointcounter.model.entity.User
import com.example.pointcounter.repository.Repository
import kotlinx.coroutines.launch
import kotlin.random.Random

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

    fun getRandomLightColor(): Int {
        val rnd = Random.Default
        return Color.argb(255,
            rnd.nextInt(256)/2 + 125,
            rnd.nextInt(256)/2 + 125,
            rnd.nextInt(256)/2 + 125)
    }

    fun resetAllUsersPoint(users: List<User>) = viewModelScope.launch {
        for (element in users) {
            element.score = 0
            updateUser(element)
        }
    }
}
