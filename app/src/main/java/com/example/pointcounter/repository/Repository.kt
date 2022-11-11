package com.example.pointcounter.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.pointcounter.model.dao.UserDao
import com.example.pointcounter.model.entity.User
import kotlin.random.Random

class Repository (private val dao: UserDao) {

    private val rnd = Random.Default
    private var dice = mutableListOf("1","2","3","4","5","6")
    val diceResult: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    val listUsers = dao.getUsers()

    suspend fun addUser(user: User) = dao.addUser(user)

    suspend fun updateUser(user: User) = dao.updateUser(user)

    suspend fun deleteUser(user: User) = dao.deleteUser(user)

    suspend fun deleteAllUsers() = dao.deleteAllUsers()

    fun setDice(list: List<String>) {
        Log.e("MY-LOG", "dice list : $list")
        dice.clear()
        dice.addAll(list)
    }

    fun launchDice() {
        diceResult.value = dice[rnd.nextInt(dice.size)]
    }
}