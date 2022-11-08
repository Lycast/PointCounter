package com.example.pointcounter.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.pointcounter.databinding.CounterCompactListItemBinding
import com.example.pointcounter.databinding.CounterListItemBinding
import com.example.pointcounter.model.entity.User
import com.example.pointcounter.utils.ViewHolderEnum

class UserAdapter(private val users : List<User>, private val listener: OnItemClickListener, private val enum: ViewHolderEnum) : RecyclerView.Adapter<ViewHolder>() {

    private lateinit var bindingList: CounterListItemBinding
    private lateinit var bindingCompactList: CounterCompactListItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when ( enum ) {
            ViewHolderEnum.LIST -> {
                bindingList = CounterListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                CounterListViewHolder(parent.context, bindingList, listener)
            }
            ViewHolderEnum.COMPACT_LIST -> {
                bindingCompactList = CounterCompactListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                CounterCompactListViewHolder(parent.context, bindingCompactList, listener)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (enum) {
            ViewHolderEnum.LIST -> (holder as CounterListViewHolder).adapterView(users[position])
            ViewHolderEnum.COMPACT_LIST -> (holder as CounterCompactListViewHolder).adapterView(users[position])
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }
}