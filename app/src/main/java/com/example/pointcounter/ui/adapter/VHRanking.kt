package com.example.pointcounter.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.pointcounter.databinding.ItemListRankingBinding
import com.example.pointcounter.model.entity.User
import com.example.pointcounter.utils.EnumItem
import com.example.pointcounter.utils.OnItemClickListener

class VHRanking(private val itemBinding: ItemListRankingBinding, private val listener: OnItemClickListener, private var game: Boolean): RecyclerView.ViewHolder(itemBinding.root) {

    fun adapterView(user: User) {

        // binding view
        itemBinding.itemTvName.text = user.name
        itemBinding.itemTvScore.text = user.score.toString()
        itemBinding.itemCard.setCardBackgroundColor(user.color)

        if (game) itemBinding.itemIvEdit.visibility = View.GONE
        itemBinding.itemIvEdit.setOnClickListener { listener.setOnItemClickListener(user, EnumItem.EDIT, 0) }
    }
}