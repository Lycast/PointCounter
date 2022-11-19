package com.example.scorecounter.ui.adapter.counter

import android.content.Context
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.scorecounter.R
import com.example.scorecounter.databinding.ItemStyleCompactBinding
import com.example.scorecounter.model.entity.User
import com.example.scorecounter.utils.EnumItem
import com.example.scorecounter.utils.OnItemClickListener

class VHCountSize2(private val context: Context, private val listener: OnItemClickListener, private val binding: ItemStyleCompactBinding
              ): RecyclerView.ViewHolder(binding.root) {

    fun adapterView(user: User, pos: Int) {

        binding.apply {
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
                popupMenu.menu.add(context.getString(R.string.edit)).setOnMenuItemClickListener {
                    listener.setOnItemClickListener(user, EnumItem.EDIT, pos)
                    true
                }
                popupMenu.menu.add(context.getString(R.string.reset_counter)).setOnMenuItemClickListener {
                    listener.setOnItemClickListener(user, EnumItem.RESET_POINT, pos)
                    true
                }
                popupMenu.menu.add(context.getString(R.string.delete_counter)).setOnMenuItemClickListener {
                    listener.setOnItemClickListener(user, EnumItem.DELETE, pos)
                    true
                }
                popupMenu.show()
            }

            itemTvName.setOnLongClickListener {
                listener.setOnItemClickListener(user, EnumItem.DELETE, pos)
                return@setOnLongClickListener true
            }

            itemCard.layoutParams.height = 175
        }
    }
}