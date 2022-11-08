package com.example.pointcounter.ui.adapter

import com.example.pointcounter.model.entity.User
import com.example.pointcounter.utils.UserEnum

interface OnItemClickListener {
    fun setOnItemClickListener(user: User, enum: UserEnum)
}