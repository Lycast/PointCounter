package com.example.scorecounter.utils

import com.example.scorecounter.model.entity.User

interface OnItemClickListener {
    fun setOnItemClickListener(user: User, enum: EnumItem, pos: Int)
}