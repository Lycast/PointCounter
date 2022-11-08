package com.example.pointcounter.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_guest")
data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_guest")
    val id: Int,

    @ColumnInfo(name = "name_guest")
    val name: String,

    @ColumnInfo(name = "score_guest")
    var score : Int = 0,

    @ColumnInfo(name = "color_guest")
    var color : Int = 0
) {
    override fun toString(): String {
        return this.name
    }
}