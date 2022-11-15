package com.example.scorecounter.ui.adapter.counter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.scorecounter.databinding.*
import com.example.scorecounter.model.entity.User
import com.example.scorecounter.utils.EnumVHSelect
import com.example.scorecounter.utils.OnItemClickListener

class RVCountAdapter(private val listener: OnItemClickListener, private var enumSelected: EnumVHSelect) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {



    private lateinit var bindingS1: ItemCounterMediumBinding
    private lateinit var bindingS2: ItemStyleCompactBinding
    private lateinit var bindingS3: ItemCounterSmallBinding

    private var users: List<User> = arrayListOf()

    fun setData(usersList: List<User>) {
        this.users = usersList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when ( enumSelected ) {
            EnumVHSelect.SIZE_1 -> {
                bindingS1 = ItemCounterMediumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                VHCountSize1(parent.context, listener, bindingS1)
            }
            EnumVHSelect.SIZE_2 -> {
                bindingS2 = ItemStyleCompactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                VHCountSize2(parent.context, listener, bindingS2)
            }
            EnumVHSelect.SIZE_3 -> {
                bindingS3 = ItemCounterSmallBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                VHCountSize3(parent.context, listener, bindingS3)
            }
            else -> { throw IllegalArgumentException("EnumVHSelect Not found") }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when ( enumSelected ) {
            EnumVHSelect.SIZE_1 -> {
                (holder as VHCountSize1).adapterView(users[position], position)
            }
            EnumVHSelect.SIZE_2 -> {
                (holder as VHCountSize2).adapterView(users[position], position)
            }
            EnumVHSelect.SIZE_3 -> {
                (holder as VHCountSize3).adapterView(users[position], position)
            }
            else ->{}
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }
}