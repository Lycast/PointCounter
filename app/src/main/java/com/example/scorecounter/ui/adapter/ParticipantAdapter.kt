package com.example.scorecounter.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.scorecounter.databinding.ItemCounterCompactListBinding
import com.example.scorecounter.databinding.ItemCounterListBinding
import com.example.scorecounter.databinding.ItemListHomeBinding
import com.example.scorecounter.databinding.ItemListRankingBinding
import com.example.scorecounter.databinding.ItemListRoundBinding
import com.example.scorecounter.model.entity.User
import com.example.scorecounter.utils.OnItemClickListener
import com.example.scorecounter.utils.EnumVHSelect

class ParticipantAdapter(private val users : List<User>, private val listener: OnItemClickListener, private val enum: EnumVHSelect, private val game: Boolean?) : RecyclerView.Adapter<ViewHolder>() {

    private lateinit var bindingHome: ItemListHomeBinding
    private lateinit var bindingRound: ItemListRoundBinding
    private lateinit var bindingRanking: ItemListRankingBinding
    private lateinit var bindingList: ItemCounterListBinding
    private lateinit var bindingCompactList: ItemCounterCompactListBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when ( enum ) {
            EnumVHSelect.LIST -> {
                bindingList = ItemCounterListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                VHList(parent.context, bindingList, listener)
            }
            EnumVHSelect.COMPACT_LIST -> {
                bindingCompactList = ItemCounterCompactListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                VHCompactList(parent.context, bindingCompactList, listener)
            }
            EnumVHSelect.HOME -> {
                bindingHome = ItemListHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                VHParticipant(bindingHome, listener)
            }
            EnumVHSelect.ROUND -> {
                bindingRound = ItemListRoundBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                VHRound(bindingRound, listener)
            }
            EnumVHSelect.RANKING -> {
                bindingRanking = ItemListRankingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                VHRanking(bindingRanking,listener, game!!)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (enum) {
            EnumVHSelect.LIST -> (holder as VHList).adapterView(users[position], position)
            EnumVHSelect.COMPACT_LIST -> (holder as VHCompactList).adapterView(users[position], position)
            EnumVHSelect.HOME -> (holder as VHParticipant).adapterView(users[position], position)
            EnumVHSelect.ROUND -> (holder as VHRound).adapterView(users[position], position)
            EnumVHSelect.RANKING -> (holder as VHRanking).adapterView(users[position])
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }
}