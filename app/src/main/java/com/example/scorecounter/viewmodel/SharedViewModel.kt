package com.example.scorecounter.viewmodel

import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scorecounter.model.ListOfName
import com.example.scorecounter.model.entity.User
import com.example.scorecounter.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

class SharedViewModel (private val repository: Repository) : ViewModel() {

    private var names = ListOfName().listNames
    lateinit var currentUser: User
    val rvSize = MutableLiveData(0)
    val listOfTournament = repository.listTournament
    val color: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    val step = MutableLiveData(1)

    fun updateListOfTournament(list: List<User>) { repository.updateListOfTournament(list) }

    fun updateRVSize(size: Int) {
        if (rvSize.value != size) rvSize.value = size }

    fun updateListDicesResult(list: List<Int>) { repository.updateListDicesResult(list) }

    fun getRndName(): String {
        if (names.size != 0) {
            val pos = (0 until names.size).random()
            val name = names[pos]
            names.removeAt(pos)
            return name
        }
        names = ListOfName().listNames
        return "Olaf"
    }

    fun getRndColor(): Int {
        val rnd = Random.Default
        return Color.argb(255, (rnd.nextInt(256) / 1.5 + 80).toInt(),
            (rnd.nextInt(256) / 1.5 + 80).toInt(), (rnd.nextInt(256) / 1.5 + 80).toInt())
    }

    fun setColor(red: Int, green: Int, blue: Int ) {
        color.value = Color.argb(255, red, green, blue)
    }

    fun setStep(newStep: Int) { step.value = newStep }


//          ----- DICE -----
    val listDicesResult = repository.listDicesResult
    val sideNumber = repository.sideNumber
    val diceNumber = repository.diceNumber

    fun launchDice(nbOfSide: Int) = (1..nbOfSide).random()

    fun setDiceNumber(numberOfDice: Int) { diceNumber.value = numberOfDice }

    fun setSideNumber(numberOfSide: Int) { sideNumber.value = numberOfSide }

//          ----- DAO -----
    val users = repository.listUsers

    fun addUser(user: User) = viewModelScope.launch { repository.addUser(user) }

    fun updateUser(user: User) = viewModelScope.launch { repository.updateUser(user) }

    fun deleteUser(user: User) = viewModelScope.launch { repository.deleteUser(user) }

    fun deleteAllUsers() = viewModelScope.launch { repository.deleteAllUsers() }

    fun resetAllUsersPoint(list: List<User>) = CoroutineScope(Dispatchers.IO).launch {
        suspend {
            for (element in list) {
                element.score = 0
                updateUser(element)
            }
        }.invoke()
    }
}
