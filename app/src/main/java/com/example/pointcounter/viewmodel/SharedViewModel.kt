package com.example.pointcounter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pointcounter.model.entity.Guest
import com.example.pointcounter.repository.Repository
import kotlinx.coroutines.launch

class SharedViewModel (private val repository: Repository) : ViewModel() {

    val guests = repository.listGuests

    fun addGuest(guest: Guest) = viewModelScope.launch {
        val idNewGuest = repository.addGuest(guest)
    }

    fun updateGuest(guest: Guest) = viewModelScope.launch {
        val nbLineUpdate = repository.updateGuest(guest)
    }

    fun deleteGuest(guest: Guest) = viewModelScope.launch {
        val nbLineDelete = repository.deleteGuest(guest)
    }

    fun deleteAllGuest() = viewModelScope.launch {
        val nbLineDeleteAll = repository.deleteAllGuest()
    }

}
//    fun getScoreGuest(guestNumber: Int) : Int {
//        return repository.getScoreGuest(guestNumber)
//    }
//
//    fun addPoint(guestNumber: Int) {
//        repository.addPoint(guestNumber)
//    }
//
//    fun removePoint(guestNumber: Int) {
//        repository.removePoint(guestNumber)
//    }
