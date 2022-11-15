package com.example.scorecounter.ui.adapter.counter

import android.content.Context
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.scorecounter.databinding.ItemCounterMediumBinding
import com.example.scorecounter.model.entity.User
import com.example.scorecounter.utils.EnumItem
import com.example.scorecounter.utils.OnItemClickListener

class VHCountSize1(private val context: Context, private val listener: OnItemClickListener, private val bindingMedium: ItemCounterMediumBinding
              ): RecyclerView.ViewHolder(bindingMedium.root) {

    fun adapterView(user: User, pos: Int) {

        bindingMedium.apply {
            itemTvName.text = user.name
            itemTvScore.text = user.score.toString()
            itemCard.setCardBackgroundColor(user.color)

            itemIvAdd.setOnClickListener {
                listener.setOnItemClickListener(user, EnumItem.ADD_POINT, pos)
            }

            itemIvRemove.setOnClickListener {
                listener.setOnItemClickListener(user, EnumItem.REMOVE_POINT, pos)
            }

            itemTvScore.setOnClickListener {
                listener.setOnItemClickListener(user, EnumItem.SET_POINT, pos)
            }

            itemTvName.setOnClickListener {
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

            itemTvName.setOnLongClickListener {
                listener.setOnItemClickListener(user, EnumItem.DELETE, pos)
                return@setOnLongClickListener true
            }
        }
    }
}