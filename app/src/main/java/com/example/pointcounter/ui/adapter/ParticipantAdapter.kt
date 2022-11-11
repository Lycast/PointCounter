package com.example.pointcounter.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.pointcounter.databinding.ItemCounterCompactListBinding
import com.example.pointcounter.databinding.ItemCounterListBinding
import com.example.pointcounter.databinding.ItemListHomeBinding
import com.example.pointcounter.model.entity.User
import com.example.pointcounter.utils.OnItemClickListener
import com.example.pointcounter.utils.ViewHolderEnum

class ParticipantAdapter(private val users : List<User>, private val listener: OnItemClickListener, private val enum: ViewHolderEnum) : RecyclerView.Adapter<ViewHolder>() {

    private lateinit var bindingHome: ItemListHomeBinding
    private lateinit var bindingList: ItemCounterListBinding
    private lateinit var bindingCompactList: ItemCounterCompactListBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when ( enum ) {
            ViewHolderEnum.LIST -> {
                bindingList = ItemCounterListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ViewHolderCounterList(parent.context, bindingList, listener)
            }
            ViewHolderEnum.COMPACT_LIST -> {
                bindingCompactList = ItemCounterCompactListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ViewHolderCounterCompactList(parent.context, bindingCompactList, listener)
            }
            ViewHolderEnum.HOME -> {
                bindingHome = ItemListHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ViewHolderParticipant(bindingHome, listener)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (enum) {
            ViewHolderEnum.LIST -> (holder as ViewHolderCounterList).adapterView(users[position])
            ViewHolderEnum.COMPACT_LIST -> (holder as ViewHolderCounterCompactList).adapterView(users[position])
            ViewHolderEnum.HOME -> (holder as ViewHolderParticipant).adapterView(users[position])
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }
}