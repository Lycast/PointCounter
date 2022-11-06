package com.example.pointcounter.ui.adapter

import com.example.pointcounter.model.entity.Guest

interface OnItemClickListener {
    fun setOnItemClickListener(guest: Guest, enum: AdapterEnum )
}