package com.example.pointcounter.repository

import com.example.pointcounter.model.dao.GuestDao
import com.example.pointcounter.model.entity.Guest

class Repository (private val dao: GuestDao) {

    val listGuests = dao.getGuests()

    suspend fun addGuest(guest: Guest) = dao.addGuest(guest)

    suspend fun updateGuest(guest: Guest) = dao.updateGuest(guest)

    suspend fun deleteGuest(guest: Guest) = dao.deleteGuest(guest)

    suspend fun deleteAllGuest() = dao.deleteAllGuest()
}