package com.example.pointcounter.repository

import com.example.pointcounter.model.dao.UserDao
import com.example.pointcounter.model.entity.User

class Repository (private val dao: UserDao) {

    val listUsers = dao.getUsers()

    suspend fun addUser(user: User) = dao.addUser(user)

    suspend fun updateUser(user: User) = dao.updateUser(user)

    suspend fun deleteUser(user: User) = dao.deleteUser(user)

    suspend fun deleteAllUsers() = dao.deleteAllUsers()

}