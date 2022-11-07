package com.example.pointcounter.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pointcounter.model.dao.UserDao
import com.example.pointcounter.model.entity.User

@Database(entities = [User::class], version = 1)
abstract class UserRoomDatabase : RoomDatabase() {

    abstract val userDao : UserDao

    companion object {
        @Volatile
        private var INSTANCE : UserRoomDatabase? = null

        fun getInstance(context: Context) : UserRoomDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(context, UserRoomDatabase::class.java, "BDGuests").build()
                }
                return instance
            }
        }
    }
}