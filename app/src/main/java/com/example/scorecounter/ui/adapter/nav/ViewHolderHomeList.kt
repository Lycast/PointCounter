package com.example.scorecounter.ui.adapter.nav

import android.content.Context
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.scorecounter.databinding.ItemStyleCompactBinding
import com.example.scorecounter.model.entity.User
import com.example.scorecounter.utils.EnumItem
import com.example.scorecounter.utils.OnItemClickListener

class ViewHolderHomeList(private val context: Context, private val itemBinding: ItemStyleCompactBinding, private val listener: OnItemClickListener): RecyclerView.ViewHolder(itemBinding.root) {

    fun adapterView(user: User, pos: Int) {

        itemBinding.apply {

            // binding text
            itemTvName.text = user.name
            itemCard.setCardBackgroundColor(user.color)

            // binding click
            itemTvName.setOnLongClickListener {
                listener.setOnItemClickListener(user, EnumItem.DELETE, pos)
                return@setOnLongClickListener true
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

            llScore.visibility = View.GONE
        }
    }
}