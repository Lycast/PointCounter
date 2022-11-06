package com.example.pointcounter.model.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.pointcounter.model.entity.Guest

@Dao
interface GuestDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addGuest(guest: Guest) : Long

    @Update
    suspend fun updateGuest(guest: Guest) : Int

    @Delete
    suspend fun deleteGuest(guest: Guest) : Int

    @Query ("DELETE FROM table_guest")
    suspend fun deleteAllGuest() : Int

    @Query ("SELECT * FROM table_guest ")
    fun getGuests() : LiveData<List<Guest>>
}