package com.example.scorecounter.ui.adapter.list

import android.content.Context
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.scorecounter.databinding.ItemStyleCompactBinding
import com.example.scorecounter.model.entity.User
import com.example.scorecounter.utils.EnumItem
import com.example.scorecounter.utils.OnItemClickListener

class VHList(private val context: Context, private val itemBinding: ItemStyleCompactBinding, private val listener: OnItemClickListener): RecyclerView.ViewHolder(itemBinding.root) {

    fun adapterView(user: User) {

        itemBinding.apply {

            itemTvName.text = user.name
            itemCard.setCardBackgroundColor(user.color)


            itemTvName.setOnLongClickListener {
                listener.setOnItemClickListener(user, EnumItem.DELETE, null)
                return@setOnLongClickListener true
            }

            itemTvName.setOnClickListener {
                val popupMenu = PopupMenu(context, it)
                popupMenu.menu.add("Edit").setOnMenuItemClickListener {
                    listener.setOnItemClickListener(user, EnumItem.EDIT, null)
                    true
                }
                popupMenu.menu.add("Delete").setOnMenuItemClickListener {
                    listener.setOnItemClickListener(user, EnumItem.DELETE, null)
                    true
                }
                popupMenu.show()
            }

            itemTvName.setOnLongClickListener {
                listener.setOnItemClickListener(user, EnumItem.DELETE, null)
                return@setOnLongClickListener true
            }

            itemIvAdd.setOnClickListener { listener.setOnItemClickListener(user, EnumItem.SELECT, null) }
            itemIvRemove.visibility = View.GONE
            itemTvScore.visibility = View.GONE
            llScore.layoutParams.width =  300
        }
    }
}