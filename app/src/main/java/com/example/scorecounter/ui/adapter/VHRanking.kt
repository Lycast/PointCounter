package com.example.scorecounter.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.scorecounter.databinding.ItemListRankingBinding
import com.example.scorecounter.model.entity.User
import com.example.scorecounter.utils.EnumItem
import com.example.scorecounter.utils.OnItemClickListener

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