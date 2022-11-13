package com.example.pointcounter.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.pointcounter.databinding.ItemListHomeBinding
import com.example.pointcounter.model.entity.User
import com.example.pointcounter.utils.OnItemClickListener
import com.example.pointcounter.utils.EnumItem

class VHParticipant(private val itemBinding: ItemListHomeBinding, private val listener: OnItemClickListener): RecyclerView.ViewHolder(itemBinding.root) {

    fun adapterView(user: User, pos: Int) {

        // binding text
        itemBinding.itemTextViewName.text = user.name
        itemBinding.itemCard.setCardBackgroundColor(user.color)

        // binding click
        itemBinding.itemImageViewClear.setOnClickListener { listener.setOnItemClickListener(user, EnumItem.DELETE, pos) }

        itemBinding.itemImageViewEdit.setOnClickListener { listener.setOnItemClickListener(user, EnumItem.EDIT, pos) }
        itemBinding.itemTextViewName.setOnClickListener { listener.setOnItemClickListener(user, EnumItem.EDIT, pos) }
    }
}