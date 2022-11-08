package com.example.pointcounter.ui

import android.os.Bundle
import android.text.TextUtils
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.pointcounter.R
import com.example.pointcounter.data.UserRoomDatabase
import com.example.pointcounter.databinding.ActivityPointCounterListBinding
import com.example.pointcounter.databinding.PopupAddGuestBinding
import com.example.pointcounter.model.entity.User
import com.example.pointcounter.repository.Repository
import com.example.pointcounter.ui.adapter.OnItemClickListener
import com.example.pointcounter.ui.adapter.UserAdapter
import com.example.pointcounter.ui.adapter.UserEnum
import com.example.pointcounter.viewmodel.SharedViewModel
import com.example.pointcounter.viewmodel.SharedViewModelFactory
import com.google.android.material.snackbar.Snackbar

class PointCounterListActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var binding: ActivityPointCounterListBinding
    private lateinit var adapter : UserAdapter
    private lateinit var viewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPointCounterListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dao = UserRoomDatabase.getInstance(application).userDao
        val repository = Repository(dao)
        val factory = SharedViewModelFactory(repository)

        viewModel = ViewModelProvider(this, factory)[SharedViewModel::class.java]

        displayList()
        setOnClickMenuToolbar()
    }

    override fun setOnItemClickListener(user: User, enum: UserEnum) {
        when ( enum ) {
            UserEnum.DELETE -> viewModel.deleteUser(user)
            UserEnum.EDIT -> setDisplayPopupAddUser(user)
            UserEnum.RESET_POINT -> {
                user.score = 0
                viewModel.updateUser(user)
            }
            UserEnum.ADD_1_POINT -> {
                user.score ++
                viewModel.updateUser(user)
            }
            UserEnum.ADD_10_POINT -> {
                user.score += 10
                viewModel.updateUser(user)
            }
            UserEnum.REMOVE_1_POINT -> {
                user.score --
                viewModel.updateUser(user)
            }
            UserEnum.REMOVE_10_POINT -> {
                user.score -= 10
                viewModel.updateUser(user)
            }
        }
    }

    private fun setDisplayPopupAddUser(user: User?) {
        var color = user?.color ?: viewModel.getRandomLightColor()
        val alertDialog = AlertDialog.Builder(this)
        val dialogBinding = PopupAddGuestBinding.inflate(layoutInflater)
        alertDialog.setView(dialogBinding.root)

        dialogBinding.cardExampleColor.setCardBackgroundColor(color)
        //dialogBinding.buttonAddNewGuest.setTextColor(color)
        dialogBinding.colorChoice.setTextColor(color)

        dialogBinding.colorChoice.setOnClickListener {
            color = viewModel.getRandomLightColor()
            dialogBinding.cardExampleColor.setCardBackgroundColor(color)
            //dialogBinding.buttonAddNewGuest.setTextColor(color)
            dialogBinding.colorChoice.setTextColor(color)
        }

        val dialog = alertDialog.create()

        dialogBinding.buttonAddNewGuest.setOnClickListener {
                if (user == null && !TextUtils.isEmpty(dialogBinding.editTextEnterName.text)) {
                    //  Add new guest
                    val newUser = User(0, dialogBinding.editTextEnterName.text.toString(), 0, color)
                    viewModel.addUser(newUser)
                    dialog.dismiss()
                } else if (user != null && !TextUtils.isEmpty(dialogBinding.editTextEnterName.text)) {
                    // Update guest
                    viewModel.updateUser(User(user.id, dialogBinding.editTextEnterName.text.toString(), user.score, color))
                    dialog.dismiss()
                } else {
                    Snackbar.make(dialogBinding.root, "The name must be filled in", Snackbar.LENGTH_SHORT)
                        .setBackgroundTint(ContextCompat.getColor(this, R.color.light_green))
                        .show()
                }
            }
        dialog.show()
    }

    private fun displayList() {
        viewModel.users.observe(this) {
            adapter = UserAdapter(it, this)
            binding.recyclerViewGuests.adapter = adapter
        }
    }

    private fun setOnClickMenuToolbar() {
        val toolbarBackImg: ImageView = findViewById(R.id.toolbar_image_view_back)
        val toolbarMenu: ImageView = findViewById(R.id.toolbar_image_view_menu)
        val toolbarAdd: ImageView = findViewById(R.id.toolbar_image_view_add)

        toolbarBackImg.setOnClickListener {
            viewModel.users.observe(this) {
                viewModel.resetAllUsersPoint(it)
            }
            finish()
        }
        toolbarAdd.setOnClickListener { setDisplayPopupAddUser(null) }
        toolbarMenu.setOnClickListener {
            val popupMenu = PopupMenu(this, it)
            popupMenu.menu.add("Delete all counters").setOnMenuItemClickListener {
                viewModel.deleteAllUsers()
                true
            }
            popupMenu.menu.add("Reset all counters").setOnMenuItemClickListener {
                viewModel.deleteAllUsers()
                true
            }
            popupMenu.show()
        }
    }
}