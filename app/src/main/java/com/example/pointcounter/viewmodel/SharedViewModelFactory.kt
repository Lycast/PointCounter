package com.example.pointcounter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pointcounter.repository.Repository

class SharedViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SharedViewModel::class.java))
            return SharedViewModel(repository) as T
        throw IllegalArgumentException("Class ViewModel not found")
    }
}