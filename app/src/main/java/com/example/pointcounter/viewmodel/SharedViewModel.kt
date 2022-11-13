package com.example.pointcounter.viewmodel

import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pointcounter.model.ListOfName
import com.example.pointcounter.model.entity.User
import com.example.pointcounter.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

class SharedViewModel (private val repository: Repository) : ViewModel() {

    private var names = ListOfName().listNames
    val users = repository.listUsers
    val listOfTournament = repository.listTournament
    var diceResult = MutableLiveData((1..6).random())
    val color: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    val step = MutableLiveData(1)


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

    fun updateListOfTournament(list: List<User>) { repository.updateListOfTournament(list) }

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

//    fun setDice(list: List<String>) { repository.setDice(list) }

    fun launchNormalDice() { diceResult.value = (1..6).random() }

    fun setStep(newStep: Int) { step.value = newStep }
}
