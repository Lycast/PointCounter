package com.example.scorecounter.model.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.scorecounter.model.entity.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User) : Long

    @Update
    suspend fun updateUser(user: User) : Int

    @Delete
    suspend fun deleteUser(user: User) : Int

    @Query ("DELETE FROM table_guest")
    suspend fun deleteAllUsers() : Int

    @Query ("SELECT * FROM table_guest ")
    fun getLiveDataListUser() : LiveData<List<User>>

    @Query ("SELECT * FROM table_guest ")
    suspend fun getListUser() : MutableList<User>

    @Query ("SELECT * FROM table_guest WHERE id_guest IN (:list) ")
    suspend fun getUsersSelected(list: MutableList<Int>) : MutableList<User>
}