package com.example.pointcounter.ui

import android.content.res.Configuration
import android.os.Bundle
import android.text.TextUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pointcounter.R
import com.example.pointcounter.data.UserRoomDatabase
import com.example.pointcounter.databinding.ActivityCounterListBinding
import com.example.pointcounter.databinding.AlertDialogParticipantBinding
import com.example.pointcounter.model.entity.User
import com.example.pointcounter.repository.Repository
import com.example.pointcounter.ui.adapter.OnItemClickListener
import com.example.pointcounter.ui.adapter.UserAdapter
import com.example.pointcounter.utils.UserEnum
import com.example.pointcounter.utils.ViewHolderEnum
import com.example.pointcounter.viewmodel.SharedViewModel
import com.example.pointcounter.viewmodel.SharedViewModelFactory
import com.google.android.material.snackbar.Snackbar

class CounterListActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var binding: ActivityCounterListBinding
    private lateinit var adapter : UserAdapter
    private lateinit var viewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCounterListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dao = UserRoomDatabase.getInstance(application).userDao
        val repository = Repository(dao)
        val factory = SharedViewModelFactory(repository)

        viewModel = ViewModelProvider(this, factory)[SharedViewModel::class.java]

        displayList()
        setToolbar()
    }

    override fun setOnItemClickListener(user: User, enum: UserEnum) {
        when ( enum ) {
            UserEnum.DELETE -> viewModel.deleteUser(user)
            UserEnum.EDIT -> setDisplayAlertDialogParticipant(user)
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

    private fun setDisplayAlertDialogParticipant(user: User?) {
        var color = user?.color ?: viewModel.getRandomLightColor()
        val alertDialog = AlertDialog.Builder(this)
        val dialogBinding = AlertDialogParticipantBinding.inflate(layoutInflater)
        alertDialog.setView(dialogBinding.root)

        // Populate alert dialog
        if (user == null) {
            dialogBinding.cardExampleColor.setCardBackgroundColor(color)
            dialogBinding.btnGenerateColor.setTextColor(color)
        } else {
            dialogBinding.alertDialogTitle.setText(R.string.edit_participant)
            dialogBinding.editTextEnterName.setText(user.name)
            dialogBinding.btnGenerateColor.setTextColor(user.color)
            dialogBinding.cardExampleColor.setCardBackgroundColor(user.color)
        }

        val dialog = alertDialog.create()

        // On click button generate color
        dialogBinding.btnGenerateColor.setOnClickListener {
            color = viewModel.getRandomLightColor()
            dialogBinding.cardExampleColor.setCardBackgroundColor(color)
            dialogBinding.btnGenerateColor.setTextColor(color)
        }

        dialogBinding.alertDialogButtonOk.setOnClickListener {
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
                Snackbar.make(dialogBinding.root, "The name must be filled in", Snackbar.LENGTH_SHORT).show()
            }
        }
        dialog.show()
    }

    private fun displayList() {
        viewModel.users.observe(this) {
            adapter = UserAdapter(it, this, ViewHolderEnum.LIST)
            binding.recyclerViewGuests.adapter = adapter

            var column = 1
            if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) { column = 2 }
            binding.recyclerViewGuests.layoutManager = GridLayoutManager( this, column)
        }
    }

    private fun setToolbar() {
        val toolbarBackImg: ImageView = findViewById(R.id.toolbar_image_view_back)
        val toolbarMenu: ImageView = findViewById(R.id.toolbar_image_view_menu)
        val toolbarAdd: ImageView = findViewById(R.id.toolbar_image_view_add)
        val toolbarTitle: TextView = findViewById(R.id.toolbar_title)

        toolbarTitle.setText(R.string.list_counter)
        toolbarBackImg.setOnClickListener {
            viewModel.users.observe(this) {
                viewModel.resetAllUsersPoint(it)
            }
            finish()
        }
        toolbarAdd.setOnClickListener { setDisplayAlertDialogParticipant(null) }
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