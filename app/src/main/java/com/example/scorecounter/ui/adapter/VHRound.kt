package com.example.scorecounter.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.scorecounter.databinding.ItemListRoundBinding
import com.example.scorecounter.model.entity.User
import com.example.scorecounter.utils.EnumItem
import com.example.scorecounter.utils.OnItemClickListener

class VHRound(private val itemBinding: ItemListRoundBinding, private val listener: OnItemClickListener): RecyclerView.ViewHolder(itemBinding.root) {

    fun adapterView(user: User, pos: Int) {

        // binding view
        itemBinding.itemTvName.text = user.name
        itemBinding.itemCard.setCardBackgroundColor(user.color)

        // binding click
        itemBinding.itemCard.setOnLongClickListener {
            listener.setOnItemClickListener(user, EnumItem.ROUND_WIN, pos)
            return@setOnLongClickListener true
        }
    }
}