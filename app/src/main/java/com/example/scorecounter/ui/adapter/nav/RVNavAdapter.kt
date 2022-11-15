package com.example.scorecounter.ui.adapter.nav

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.scorecounter.databinding.ItemStyleCompactBinding
import com.example.scorecounter.model.entity.User
import com.example.scorecounter.utils.OnItemClickListener
import com.example.scorecounter.utils.EnumVHSelect

class RVNavAdapter(private val users : List<User>, private val listener: OnItemClickListener, private val enum: EnumVHSelect, private val game: Boolean?) : RecyclerView.Adapter<ViewHolder>() {

    private lateinit var bindingItem: ItemStyleCompactBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when ( enum ) {
            EnumVHSelect.HOME -> {
                bindingItem = ItemStyleCompactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ViewHolderHomeList(parent.context, bindingItem, listener)
            }
            EnumVHSelect.ROUND -> {
                bindingItem = ItemStyleCompactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                VHRound(bindingItem, listener)
            }
            EnumVHSelect.RANKING -> {
                bindingItem = ItemStyleCompactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                VHRanking(parent.context, bindingItem,listener, game!!)
            }
            else -> { throw IllegalArgumentException("EnumVHSelect Not found") }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (enum) {
            EnumVHSelect.HOME -> (holder as ViewHolderHomeList).adapterView(users[position], position)
            EnumVHSelect.ROUND -> (holder as VHRound).adapterView(users[position], position)
            EnumVHSelect.RANKING -> (holder as VHRanking).adapterView(users[position], position)
            else -> {}
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }
}