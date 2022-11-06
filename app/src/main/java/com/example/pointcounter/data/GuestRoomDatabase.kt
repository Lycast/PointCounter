package com.example.pointcounter.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pointcounter.model.dao.GuestDao
import com.example.pointcounter.model.entity.Guest

@Database(entities = [Guest::class], version = 1)
abstract class GuestRoomDatabase : RoomDatabase() {

    abstract val guestDao : GuestDao

    companion object {
        @Volatile
        private var INSTANCE : GuestRoomDatabase? = null

        fun getInstance(context: Context) : GuestRoomDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(context, GuestRoomDatabase::class.java, "BDGuests").build()
                }
                return instance
            }
        }
    }
}