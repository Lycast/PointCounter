package com.example.pointcounter.utils

import com.example.pointcounter.model.entity.User

interface OnItemClickListener {
    fun setOnItemClickListener(user: User, enum: UserEnum)
}