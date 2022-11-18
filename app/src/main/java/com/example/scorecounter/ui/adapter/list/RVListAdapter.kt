package com.example.scorecounter.ui.adapter.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.scorecounter.databinding.*
import com.example.scorecounter.model.entity.User
import com.example.scorecounter.utils.OnItemClickListener

class RVListAdapter(private val listener: OnItemClickListener, private var all: Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var bindingItem: ItemStyleCompactBinding

    private var allUsers: List<User> = arrayListOf()
    private var selectedUsers: List<User> = arrayListOf()


    fun setDataAll(usersList: List<User>) {
        this.allUsers = usersList
        notifyDataSetChanged()
    }

    fun setDataSelected(usersList: List<User>) {
        this.selectedUsers = usersList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (all) {
            true -> {
                bindingItem = ItemStyleCompactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                VHList(parent.context, bindingItem, listener)
            }
            false -> {
                bindingItem = ItemStyleCompactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                VHListSelected(bindingItem, listener)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (all) {
            true -> { (holder as VHList).adapterView(allUsers[position]) }
            false -> { (holder as VHListSelected).adapterView(selectedUsers[position]) }
        }
    }

    override fun getItemCount(): Int {
        return when (all) {
            true -> allUsers.size
            false -> selectedUsers.size
        }
    }
}