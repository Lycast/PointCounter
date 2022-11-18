package com.example.scorecounter.ui.adapter.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.scorecounter.databinding.ItemStyleCompactBinding
import com.example.scorecounter.model.entity.User
import com.example.scorecounter.utils.EnumItem
import com.example.scorecounter.utils.OnItemClickListener

class VHListSelected(private val itemBinding: ItemStyleCompactBinding, private val listener: OnItemClickListener): RecyclerView.ViewHolder(itemBinding.root) {

    fun adapterView(user: User) {

        itemBinding.apply {

            // binding text
            itemTvName.text = user.name
            itemCard.setCardBackgroundColor(user.color)

            // binding click
            itemTvName.setOnLongClickListener {
                listener.setOnItemClickListener(user, EnumItem.DELETE, null)
                return@setOnLongClickListener true
            }

            itemIvRemove.setOnClickListener { listener.setOnItemClickListener(user, EnumItem.UNSELECT, null) }
            itemIvAdd.visibility = View.GONE

            itemTvScore.visibility = View.GONE

            llScore.layoutParams.width =  250
        }
    }
}