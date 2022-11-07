package com.example.pointcounter.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.pointcounter.databinding.GuestItemBinding
import com.example.pointcounter.model.entity.User

class UserAdapter(private val users : List<User>, private val listener: OnItemClickListener) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = GuestItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(parent.context ,binding, listener)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.adapterView(users[position])
    }

    override fun getItemCount(): Int {
        return users.size
    }

    class UserViewHolder(private val context: Context, private val itemBinding: GuestItemBinding, private val listener: OnItemClickListener): RecyclerView.ViewHolder(itemBinding.root) {
        fun adapterView(user: User) {
            // binding text
            itemBinding.guestItemTextViewName.text = user.name
            itemBinding.userItemTextViewScore.text = user.score.toString()

            // binding click
            itemBinding.userItemImageViewAddPoint.setOnClickListener { listener.setOnItemClickListener(user, UserEnum.ADD_POINT) }
            itemBinding.userItemImageViewRemovePoint.setOnClickListener { listener.setOnItemClickListener(user, UserEnum.REMOVE_POINT) }
            itemBinding.guestItemImageViewMenu.setOnClickListener {
                val popupMenu = PopupMenu(context, it)
                popupMenu.menu.add("Edit").setOnMenuItemClickListener {
                    listener.setOnItemClickListener(user, UserEnum.EDIT)
                    true
                }
                popupMenu.menu.add("Reset Counter").setOnMenuItemClickListener {
                    listener.setOnItemClickListener(user, UserEnum.RESET_POINT)
                    true
                }
                popupMenu.menu.add("Delete").setOnMenuItemClickListener {
                    listener.setOnItemClickListener(user, UserEnum.DELETE)
                    true
                }
                popupMenu.show()
            }
        }
    }
}