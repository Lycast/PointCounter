package com.example.pointcounter.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.pointcounter.databinding.ItemListRoundBinding
import com.example.pointcounter.model.entity.User
import com.example.pointcounter.utils.EnumItem
import com.example.pointcounter.utils.OnItemClickListener

class VHRound(private val itemBinding: ItemListRoundBinding, private val listener: OnItemClickListener): RecyclerView.ViewHolder(itemBinding.root) {

    fun adapterView(user: User, pos: Int) {

        // binding view
        itemBinding.itemTextViewName.text = user.name
        itemBinding.itemCard.setCardBackgroundColor(user.color)

        // binding click
        itemBinding.itemCard.setOnLongClickListener {
            listener.setOnItemClickListener(user, EnumItem.ROUND_WIN, pos)
            return@setOnLongClickListener true
        }
    }
}