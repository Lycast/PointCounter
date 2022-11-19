package com.example.scorecounter.ui.adapter.game

import android.content.Context
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.scorecounter.R
import com.example.scorecounter.databinding.ItemStyleCompactBinding
import com.example.scorecounter.model.entity.User
import com.example.scorecounter.utils.EnumItem
import com.example.scorecounter.utils.OnItemClickListener

class VHRanking(
    private val context: Context,
    private val itemBinding: ItemStyleCompactBinding,
    private val listener: OnItemClickListener,
    private var game: Boolean
) : RecyclerView.ViewHolder(itemBinding.root) {

    fun adapterView(user: User, pos: Int) {

        // binding view
        itemBinding.apply {
            itemTvName.text = user.name
            itemTvScore.text = user.score.toString()
            itemCard.setCardBackgroundColor(user.color)
            itemIvRemove.visibility = View.GONE
            itemIvAdd.visibility = View.GONE
            itemTvScore.visibility = View.GONE

            if (game) itemTvScore.visibility = View.VISIBLE
            if (!game) {
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
}