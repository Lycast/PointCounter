package com.example.pointcounter.ui.adapter

import android.content.Context
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.pointcounter.databinding.ItemCounterListBinding
import com.example.pointcounter.model.entity.User
import com.example.pointcounter.utils.OnItemClickListener
import com.example.pointcounter.utils.UserEnum

class ViewHolderCounterList(private val context: Context, private val itemBinding: ItemCounterListBinding, private val listener: OnItemClickListener): RecyclerView.ViewHolder(itemBinding.root) {

    fun adapterView(user: User) {

        // binding text
        itemBinding.itemTextViewName.text = user.name
        itemBinding.userItemTextViewScore.text = user.score.toString()
        itemBinding.itemCard.setCardBackgroundColor(user.color)

        // binding click
        // Add point
        itemBinding.itemImageViewAddPoint.setOnClickListener {
            listener.setOnItemClickListener(
                user,
                UserEnum.ADD_POINT
            )
        }

        // Remove point
        itemBinding.itemImageViewRemovePoint.setOnClickListener {
            listener.setOnItemClickListener(
                user,
                UserEnum.REMOVE_POINT
            )
        }

        // Long click
        itemBinding.itemCard.setOnLongClickListener {
            listener.setOnItemClickListener(user, UserEnum.SET_POINT)
            return@setOnLongClickListener true
        }

        itemBinding.itemTextViewName.setOnClickListener {
            val popupMenu = PopupMenu(context, it)
            popupMenu.menu.add("Edit").setOnMenuItemClickListener {
                listener.setOnItemClickListener(user, UserEnum.EDIT)
                true
            }
            popupMenu.menu.add("Reset Counter").setOnMenuItemClickListener {
                listener.setOnItemClickListener(user, UserEnum.RESET_POINT)
                true
            }
            popupMenu.show()
        }
    }
}