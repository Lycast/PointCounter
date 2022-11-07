package com.example.pointcounter.ui.adapter

import com.example.pointcounter.model.entity.User

interface OnItemClickListener {
    fun setOnItemClickListener(user: User, enum: UserEnum )
}