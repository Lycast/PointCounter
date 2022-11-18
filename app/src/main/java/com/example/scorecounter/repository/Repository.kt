package com.example.scorecounter.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.scorecounter.model.dao.UserDao
import com.example.scorecounter.model.entity.User

class Repository (private val dao: UserDao) {

    //          ------ DICE ------
    val listDicesResult: MutableLiveData<List<Int>> by lazy { MutableLiveData<List<Int>>() }
    val sideNumber = MutableLiveData(6)
    val diceNumber = MutableLiveData(1)

    fun updateListDicesResult(list: List<Int>) { listDicesResult.value = list }


    //          ------ DAO ------
    val liveDataListUser: LiveData<List<User>> = dao.getLiveDataListUser()

    suspend  fun getListUserSelected(ids: MutableList<Int>): MutableList<User> = dao.getUsersSelected(ids)

    suspend fun getListUser(): MutableList<User> = dao.getListUser()

    suspend fun addUser(user: User) = dao.addUser(user)

    suspend fun updateUser(user: User) = dao.updateUser(user)

    suspend fun deleteUser(user: User) = dao.deleteUser(user)

    suspend fun deleteAllUsers() = dao.deleteAllUsers()
}