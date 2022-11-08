package com.example.pointcounter.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.pointcounter.R
import com.example.pointcounter.data.UserRoomDatabase
import com.example.pointcounter.databinding.ActivitySoloCounterBinding
import com.example.pointcounter.databinding.PopupAddGuestBinding
import com.example.pointcounter.model.entity.User
import com.example.pointcounter.repository.Repository
import com.example.pointcounter.viewmodel.SharedViewModel
import com.example.pointcounter.viewmodel.SharedViewModelFactory
import com.google.android.material.snackbar.Snackbar

class SoloCounterActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySoloCounterBinding
    private lateinit var viewModel: SharedViewModel
    private lateinit var spinner: Spinner
    private var currentUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySoloCounterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dao = UserRoomDatabase.getInstance(application).userDao
        val repository = Repository(dao)
        val factory = SharedViewModelFactory(repository)

        viewModel = ViewModelProvider(this, factory)[SharedViewModel::class.java]

        initSpinnerSelectUser()
        setClickListenerView()
        setOnClickMenuToolbar()
        setOnClickEditIcon()
    }

    private fun populateView(user: User) {
        binding.soloActivityTextViewScore.text = user.score.toString()
        binding.soloActivityCardName.setCardBackgroundColor(user.color)
    }

    private fun initSpinnerSelectUser() {
        viewModel.users.observe(this) {

            spinner = binding.soloActivitySpinnerList

            // Create an ArrayAdapter using a simple spinner layout and languages array
            val aa = ArrayAdapter(this, R.layout.spinner_item, it)
            // Set layout to use when the list of choices appear
            aa.setDropDownViewResource(R.layout.spinner_dropdown_item)
            // Set Adapter to Spinner
            spinner.adapter = aa

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    populateView(it[position])
                    currentUser = it[position]
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }
    }

    private fun setClickListenerView() {
        binding.soloActivityImageViewAddPoint.setOnClickListener {
            currentUser!!.score++
            viewModel.updateUser(currentUser!!)
        }
        binding.soloActivityImageViewAddPoint.setOnLongClickListener {
            currentUser!!.score+=10
            viewModel.updateUser(currentUser!!)
            return@setOnLongClickListener true
        }

        binding.soloActivityImageViewRemovePoint.setOnClickListener {
            currentUser!!.score--
            viewModel.updateUser(currentUser!!)
        }
        binding.soloActivityImageViewRemovePoint.setOnLongClickListener {
            currentUser!!.score -= 10
            viewModel.updateUser(currentUser!!)
            return@setOnLongClickListener true
        }
    }

    private fun setOnClickMenuToolbar() {
        val toolbarBackImg: ImageView = findViewById(R.id.toolbar_image_view_back)
        val toolbarMenu: ImageView = findViewById(R.id.toolbar_image_view_menu)
        val toolbarAdd: ImageView = findViewById(R.id.toolbar_image_view_add)

        toolbarMenu.isVisible = false
        toolbarBackImg.setOnClickListener {
            currentUser!!.score = 0
            viewModel.updateUser(currentUser!!)
            finish()
        }
        toolbarAdd.setOnClickListener { setDisplayPopupAddUser(null) }
    }

    private fun setDisplayPopupAddUser(user: User?) {
        var color = user?.color ?: viewModel.getRandomLightColor()
        val alertDialog = AlertDialog.Builder(this)
        val dialogBinding = PopupAddGuestBinding.inflate(layoutInflater)
        alertDialog.setView(dialogBinding.root)

        dialogBinding.cardExampleColor.setCardBackgroundColor(color)
        dialogBinding.colorChoice.setTextColor(color)

        dialogBinding.colorChoice.setOnClickListener {
            color = viewModel.getRandomLightColor()
            dialogBinding.cardExampleColor.setCardBackgroundColor(color)
            dialogBinding.colorChoice.setTextColor(color)
        }

        val dialog = alertDialog.create()

        dialogBinding.buttonAddNewGuest.setOnClickListener {
            if (user == null && !TextUtils.isEmpty(dialogBinding.editTextEnterName.text)) {
                //  Add new guest
                val newUser = User(0, dialogBinding.editTextEnterName.text.toString(), 0, color)
                viewModel.addUser(newUser)
                viewModel.users.observe(this) {
                    spinner.setSelection(it.size -1)
                }
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

    private fun setOnClickEditIcon() {
        binding.soloActivityImageViewMenu.setOnClickListener {
            val popupMenu = PopupMenu(this, it)
            popupMenu.menu.add("Edit").setOnMenuItemClickListener {
                setDisplayPopupAddUser(currentUser!!)
                true
            }
            popupMenu.menu.add("Reset Counter").setOnMenuItemClickListener {
                currentUser!!.score = 0
                viewModel.updateUser(currentUser!!)
                true
            }
            popupMenu.menu.add("Delete").setOnMenuItemClickListener {
                viewModel.deleteUser(currentUser!!)
                true
            }
            popupMenu.show()
        }
    }
}