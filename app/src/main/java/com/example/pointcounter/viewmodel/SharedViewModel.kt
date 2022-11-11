package com.example.pointcounter.viewmodel

import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pointcounter.model.entity.User
import com.example.pointcounter.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

class SharedViewModel (private val repository: Repository) : ViewModel() {

    private val rnd = Random.Default
    val users = repository.listUsers
    val color: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    val diceResult = repository.diceResult



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

    fun resetAllUsersPoint(list: List<User>) = CoroutineScope(Dispatchers.IO).launch {
        suspend {
            for (element in list) {
                element.score = 0
                updateUser(element)
            }
        }.invoke()
    }

    fun getRandomColor(): Int {
        return Color.argb(255,
            (rnd.nextInt(256) / 1.5 + 80).toInt(),
            (rnd.nextInt(256) / 1.5 + 80).toInt(),
            (rnd.nextInt(256) / 1.5 + 80).toInt())
    }

    fun setColor(red: Int, green: Int, blue: Int ) {
        color.value = Color.argb(255, red, green, blue)
    }

    fun setDice(list: List<String>) {
        repository.setDice(list)
    }

    fun launchDice() {
        repository.launchDice()
        }
}
