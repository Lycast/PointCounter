package com.example.scorecounter.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.scorecounter.databinding.ItemListHomeBinding
import com.example.scorecounter.model.entity.User
import com.example.scorecounter.utils.OnItemClickListener
import com.example.scorecounter.utils.EnumItem

class VHParticipant(private val itemBinding: ItemListHomeBinding, private val listener: OnItemClickListener): RecyclerView.ViewHolder(itemBinding.root) {

    fun adapterView(user: User, pos: Int) {

        // binding text
        itemBinding.itemTvName.text = user.name
        itemBinding.itemCard.setCardBackgroundColor(user.color)

        // binding click
        itemBinding.itemImageViewClear.setOnClickListener { listener.setOnItemClickListener(user, EnumItem.DELETE, pos) }

        itemBinding.itemImageViewEdit.setOnClickListener { listener.setOnItemClickListener(user, EnumItem.EDIT, pos) }
        itemBinding.itemTvName.setOnClickListener { listener.setOnItemClickListener(user, EnumItem.EDIT, pos) }
    }
}