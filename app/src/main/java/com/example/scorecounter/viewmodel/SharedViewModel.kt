package com.example.scorecounter.viewmodel

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scorecounter.model.ListOfName
import com.example.scorecounter.model.entity.User
import com.example.scorecounter.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlin.random.Random

class SharedViewModel (private val repository: Repository) : ViewModel() {

    private var names = ListOfName().listNames
    lateinit var currentUser: User
    val rvSizeDynamic = MutableLiveData(0)
    val color: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    val listOfStep = MutableLiveData(listOf(1,2,5,10))
    val step = MutableLiveData(1)
    val gameIsStarted = MutableLiveData(false)
    private val seed: Int = (Calendar.getInstance().get(Calendar.SECOND) + Calendar.getInstance().get(Calendar.MINUTE))
    private val rnd = Random(seed)

    fun updateListOfStep(list: List<Int>) { listOfStep.value = list }
    fun updateGameIsStarted(isStart: Boolean) { gameIsStarted.value = isStart }
    fun updateDynamicalRVSize(size: Int) { if (rvSizeDynamic.value != size) rvSizeDynamic.value = size }
    fun getRndName(): String {
        if (names.size != 0) {
            val pos = rnd.nextInt(names.size)
            val name = names[pos]
            names.removeAt(pos)
            return name
        }
        names = ListOfName().listNames
        return "Olaf"
    }

    fun getRndColor(): Int {
        return Color.argb(255, (rnd.nextInt(256)  / 1.5 + 80).toInt(),
            (rnd.nextInt(256)  / 1.5 + 80).toInt(), (rnd.nextInt(256)  / 1.5 + 80).toInt())
    }
    fun setColor(red: Int, green: Int, blue: Int ) {
        color.value = Color.argb(255, red, green, blue)
    }
    fun setStep(newStep: Int) { step.value = newStep }

    //          ----- DICE -----
    val listDicesResult = repository.listDicesResult
    val sideNumber = repository.sideNumber
    val diceNumber = repository.diceNumber

    fun updateListDicesResult(list: List<Int>) { repository.updateListDicesResult(list) }
    fun launchDice(nbOfSide: Int) = rnd.nextInt(nbOfSide)
    fun setDiceNumber(numberOfDice: Int) { diceNumber.value = numberOfDice }
    fun setSideNumber(numberOfSide: Int) { sideNumber.value = numberOfSide }

    //          ----- TOURNAMENT -----
    val listOfRound = MutableLiveData<List<User>>()
    val listOfRanking = MutableLiveData<List<User>>()

    fun updateListOfRound(list: List<User>) { listOfRound.value = list }
    fun updateListOfRanking(list: MutableList<User>) { listOfRanking.value = list }

    //          ----- DAO -----
    private val usersSelected: MutableList<Int> = mutableListOf()
    val liveDataListUser = repository.liveDataListUser
    val listUserSelected = MutableLiveData<List<User>>()
    val listUserUnselected = MutableLiveData<List<User>>()
    private var listDisplay: MediatorLiveData<List<User>> = MediatorLiveData()



    init {
        listDisplay.addSource(liveDataListUser) {
            combineListDisplayed(it, listUserSelected.value)
        }
        listDisplay.addSource(listUserSelected) {
            combineListDisplayed(liveDataListUser.value, it)
        }
    }

    private fun combineListDisplayed(listAll: List<User>?, listSelected: List<User>?) {
        if (listAll == null && listSelected == null) return
        if (listSelected == null || listSelected.isEmpty()) listDisplay.value = liveDataListUser.value
        else listDisplay.value = listUserSelected.value
    }

    fun updateListUnselected() {
        viewModelScope.launch {
            val newList = repository.getListUser()
            for (i in repository.getListUserSelected(usersSelected)) {
                newList.remove(i)
            }
            listUserUnselected.value = newList
        }
    }

    fun listDisplay() : LiveData<List<User>> { return listDisplay }

    private fun updateUsersSelected() = viewModelScope.launch {
        listUserSelected.value = repository.getListUserSelected(usersSelected)
    }

    fun addSelectedUser(id: Int) {
        usersSelected.add(id)
        updateUsersSelected()
        updateListUnselected()
    }
    fun removeSelectedUser(id: Int) {
        usersSelected.remove(id)
        updateUsersSelected()
        updateListUnselected()
    }
    fun addUser(user: User) = viewModelScope.launch { repository.addUser(user) }
    fun updateUser(user: User) = viewModelScope.launch { repository.updateUser(user) }
    fun deleteUser(user: User) = viewModelScope.launch { repository.deleteUser(user) }
    fun deleteAllUsers() = viewModelScope.launch {
        repository.deleteAllUsers()
        updateUsersSelected()
    }
    fun resetAllUsersPoint(list: List<User>) = CoroutineScope(Dispatchers.IO).launch {
        suspend {
            for (element in list) {
                element.score = 0
                updateUser(element)
            }
        }.invoke()
    }
}
