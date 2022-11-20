package com.example.scorecounter.ui.adapter.game

import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.example.scorecounter.databinding.ItemStyleCompactBinding
import com.example.scorecounter.model.entity.User
import com.example.scorecounter.utils.EnumItem
import com.example.scorecounter.utils.OnItemClickListener

class VHRound(private val itemBinding: ItemStyleCompactBinding, private val listener: OnItemClickListener): RecyclerView.ViewHolder(itemBinding.root) {

    fun adapterView(user: User, pos: Int) {

        // binding view
        itemBinding.apply {
            itemTvName.text = user.name
            itemCard.setCardBackgroundColor(user.color)
            llScore.visibility = View.GONE


            itemCard.layoutParams.height = 120
            itemCard.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                bottomMargin = 50
            }

            // binding click
            itemBinding.itemCard.setOnLongClickListener {
                listener.setOnItemClickListener(user, EnumItem.ROUND_WIN, pos)
                return@setOnLongClickListener true
            }
        }
    }
}