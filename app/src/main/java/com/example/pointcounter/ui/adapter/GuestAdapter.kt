package com.example.pointcounter.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.pointcounter.databinding.GuestItemBinding
import com.example.pointcounter.model.entity.Guest

class GuestAdapter(private val guests : List<Guest>, private val listener: OnItemClickListener) : RecyclerView.Adapter<GuestAdapter.GuestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuestViewHolder {
        val binding = GuestItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GuestViewHolder(parent.context ,binding, listener)
    }

    override fun onBindViewHolder(holder: GuestViewHolder, position: Int) {
        holder.adapterView(guests[position])
    }

    override fun getItemCount(): Int {
        return guests.size
    }

    class GuestViewHolder(private val context: Context, val itemBinding: GuestItemBinding, private val listener: OnItemClickListener): RecyclerView.ViewHolder(itemBinding.root) {
        fun adapterView(guest: Guest) {
            itemBinding.guestItemTextViewFirstLetter.text = guest.name?.get(0).toString()
            itemBinding.guestItemTextViewName.text = guest.name

//            itemBinding.guestItemTextViewName.setOnClickListener {
//                if (guest.isSelect == true) {
//                    listener.setOnItemClickListener(guest, AdapterEnum.SELECT)
//                    itemBinding.guestItemCard.setBackgroundColor(ContextCompat.getColor(context, R.color.green))
//                    guest.isSelect = true
//                } else {
//                    listener.setOnItemClickListener(guest, AdapterEnum.DESELECT)
//                    itemBinding.guestItemCard.setBackgroundColor(ContextCompat.getColor(context, R.color.light_gray))
//                    guest.isSelect = false
//                }

//            }

            itemBinding.guestItemImageViewMenu.setOnClickListener {
                val popupMenu = PopupMenu(context, it)
                popupMenu.menu.add("Edit").setOnMenuItemClickListener {
                    listener.setOnItemClickListener(guest, AdapterEnum.EDIT)
                    true
                }
                popupMenu.menu.add("Delete").setOnMenuItemClickListener {
                    listener.setOnItemClickListener(guest, AdapterEnum.DELETE)
                    true
                }
                popupMenu.show()
            }
        }
    }
}