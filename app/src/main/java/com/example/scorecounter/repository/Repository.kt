package com.example.scorecounter.repository

import androidx.lifecycle.MutableLiveData
import com.example.scorecounter.model.dao.UserDao
import com.example.scorecounter.model.entity.User

class Repository (private val dao: UserDao) {

//                   ------ DICE ------
    val listDicesResult: MutableLiveData<List<Int>> by lazy { MutableLiveData<List<Int>>() }
    val sideNumber = MutableLiveData(6)
    val diceNumber = MutableLiveData(1)

    fun updateListDicesResult(list: List<Int>) { listDicesResult.value = list }


//                   ------ DAO ------
    val listUsers = dao.getUsers()
    val listUserSelected: MutableLiveData<List<User>> by lazy { MutableLiveData<List<User>>() }

    suspend fun updateUsersSelected(ids: MutableList<Int>) {
        listUserSelected.value = dao.getUsersSelected(ids)
    }

    suspend fun addUser(user: User) = dao.addUser(user)

    suspend fun updateUser(user: User) = dao.updateUser(user)

    suspend fun deleteUser(user: User) = dao.deleteUser(user)

    suspend fun deleteAllUsers() = dao.deleteAllUsers()


//                 ------ TOURNAMENT ------
    val listRound: MutableLiveData<List<User>> by lazy { MutableLiveData<List<User>>() }
    val listRanking: MutableLiveData<MutableList<User>> by lazy { MutableLiveData<MutableList<User>>() }

    fun updateListOfRound(list: List<User>) { listRound.value = list }

    fun updateListOfRanking(list: MutableList<User>) { listRanking.value = list }
}