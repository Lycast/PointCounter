package com.example.scorecounter.ui.adapter

import android.content.Context
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.scorecounter.databinding.ItemCounterListBinding
import com.example.scorecounter.model.entity.User
import com.example.scorecounter.utils.OnItemClickListener
import com.example.scorecounter.utils.EnumItem

class VHList(private val context: Context, private val itemBinding: ItemCounterListBinding, private val listener: OnItemClickListener): RecyclerView.ViewHolder(itemBinding.root) {

    fun adapterView(user: User, pos: Int) {

        // binding text
        itemBinding.itemTvName.text = user.name
        itemBinding.itemTvScore.text = user.score.toString()
        itemBinding.itemCard.setCardBackgroundColor(user.color)

        // binding click
        // Add point
        itemBinding.itemIvAdd.setOnClickListener {
            listener.setOnItemClickListener(user, EnumItem.ADD_POINT, pos)
        }

        // Remove point
        itemBinding.itemIvRemove.setOnClickListener {
            listener.setOnItemClickListener(user, EnumItem.REMOVE_POINT, pos)
        }

        // Long click
        itemBinding.itemCard.setOnLongClickListener {
            listener.setOnItemClickListener(user, EnumItem.SET_POINT, pos)
            return@setOnLongClickListener true
        }

        itemBinding.itemTvName.setOnClickListener {
            val popupMenu = PopupMenu(context, it)
            popupMenu.menu.add("Edit").setOnMenuItemClickListener {
                listener.setOnItemClickListener(user, EnumItem.EDIT, pos)
                true
            }
            popupMenu.menu.add("Reset Counter").setOnMenuItemClickListener {
                listener.setOnItemClickListener(user, EnumItem.RESET_POINT, pos)
                true
            }
            popupMenu.show()
        }
    }
}