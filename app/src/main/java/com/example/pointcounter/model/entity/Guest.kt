package com.example.pointcounter.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_guest")
data class Guest(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_guest")
    val id: Int,

    @ColumnInfo(name = "name_guest")
    val name: String,

    @ColumnInfo(name = "is_selected")
    val isSelected: Boolean = false,

    @ColumnInfo(name = "score_guest")
    val score : Int = 0
)