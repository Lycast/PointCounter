package com.example.pointcounter.ui.adapter

import android.content.Context
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.pointcounter.databinding.CounterCompactListItemBinding
import com.example.pointcounter.model.entity.User
import com.example.pointcounter.utils.OnItemClickListener
import com.example.pointcounter.utils.UserEnum

class CounterCompactListViewHolder (private val context: Context, private val itemBinding: CounterCompactListItemBinding, private val listener: OnItemClickListener): RecyclerView.ViewHolder(itemBinding.root) {

    fun adapterView(user: User) {

        // binding text
        itemBinding.guestItemTextViewName.text = user.name
        itemBinding.userItemTextViewScore.text = user.score.toString()
        itemBinding.counterCompactItemCard.setCardBackgroundColor(user.color)

        // binding click
        itemBinding.counterCompactItemCard.setOnClickListener { listener.setOnItemClickListener(user, UserEnum.ADD_1_POINT) }
        itemBinding.counterCompactItemCard.setOnLongClickListener {
            listener.setOnItemClickListener(user, UserEnum.ADD_10_POINT)
            return@setOnLongClickListener true
        }

        itemBinding.userItemImageViewRemovePoint.setOnClickListener { listener.setOnItemClickListener(user, UserEnum.REMOVE_1_POINT) }
        itemBinding.userItemImageViewRemovePoint.setOnLongClickListener {
            listener.setOnItemClickListener(user, UserEnum.REMOVE_10_POINT)
            return@setOnLongClickListener true
        }

        itemBinding.guestItemImageViewMenu.setOnClickListener {
            val popupMenu = PopupMenu(context, it)
            popupMenu.menu.add("Edit").setOnMenuItemClickListener {
                listener.setOnItemClickListener(user, UserEnum.EDIT)
                true
            }
            popupMenu.menu.add("Reset Counter").setOnMenuItemClickListener {
                listener.setOnItemClickListener(user, UserEnum.RESET_POINT)
                true
            }
            popupMenu.menu.add("Delete").setOnMenuItemClickListener {
                listener.setOnItemClickListener(user, UserEnum.DELETE)
                true
            }
            popupMenu.show()
        }
    }
}