package com.example.scorecounter.repository

import androidx.lifecycle.MutableLiveData
import com.example.scorecounter.model.dao.UserDao
import com.example.scorecounter.model.entity.User

class Repository (private val dao: UserDao) {

    val listTournament: MutableLiveData<List<User>> by lazy { MutableLiveData<List<User>>() }
    val listUsers = dao.getUsers()
    val sideNumber = MutableLiveData(6)
    val diceNumber = MutableLiveData(1)

    suspend fun addUser(user: User) = dao.addUser(user)

    suspend fun updateUser(user: User) = dao.updateUser(user)

    suspend fun deleteUser(user: User) = dao.deleteUser(user)

    suspend fun deleteAllUsers() = dao.deleteAllUsers()

    fun updateListOfTournament(list: List<User>) { listTournament.value = list }
}