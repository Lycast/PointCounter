package com.example.pointcounter.ui.adapter

import android.content.Context
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.pointcounter.databinding.ItemCounterListBinding
import com.example.pointcounter.model.entity.User
import com.example.pointcounter.utils.OnItemClickListener
import com.example.pointcounter.utils.EnumItem

class VHList(private val context: Context, private val itemBinding: ItemCounterListBinding, private val listener: OnItemClickListener): RecyclerView.ViewHolder(itemBinding.root) {

    fun adapterView(user: User, pos: Int) {

        // binding text
        itemBinding.itemTextViewName.text = user.name
        itemBinding.userItemTextViewScore.text = user.score.toString()
        itemBinding.itemCard.setCardBackgroundColor(user.color)

        // binding click
        // Add point
        itemBinding.itemImageViewAddPoint.setOnClickListener {
            listener.setOnItemClickListener(user, EnumItem.ADD_POINT, pos)
        }

        // Remove point
        itemBinding.itemImageViewRemovePoint.setOnClickListener {
            listener.setOnItemClickListener(user, EnumItem.REMOVE_POINT, pos)
        }

        // Long click
        itemBinding.itemCard.setOnLongClickListener {
            listener.setOnItemClickListener(user, EnumItem.SET_POINT, pos)
            return@setOnLongClickListener true
        }

        itemBinding.itemTextViewName.setOnClickListener {
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