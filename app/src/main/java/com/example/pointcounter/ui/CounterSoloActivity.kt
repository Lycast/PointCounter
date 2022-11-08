package com.example.pointcounter.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.pointcounter.R
import com.example.pointcounter.data.UserRoomDatabase
import com.example.pointcounter.databinding.ActivityCounterSoloBinding
import com.example.pointcounter.databinding.AlertDialogParticipantBinding
import com.example.pointcounter.model.entity.User
import com.example.pointcounter.repository.Repository
import com.example.pointcounter.viewmodel.SharedViewModel
import com.example.pointcounter.viewmodel.SharedViewModelFactory
import com.google.android.material.snackbar.Snackbar

class CounterSoloActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCounterSoloBinding
    private lateinit var viewModel: SharedViewModel
    private lateinit var spinner: Spinner
    private var currentUser: User? = null
    private var spinnerPos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCounterSoloBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dao = UserRoomDatabase.getInstance(application).userDao
        val repository = Repository(dao)
        val factory = SharedViewModelFactory(repository)

        viewModel = ViewModelProvider(this, factory)[SharedViewModel::class.java]

        initSpinnerSelectParticipant()
        setClickListenerPointView()
        setToolbar()
        setOnClickEditIcon()

        if (savedInstanceState != null) {
            spinnerPos = savedInstanceState.getInt("spinner_position", 0)
        }
    }

    private fun populateView(user: User) {
        binding.soloActivityTextViewScore.text = user.score.toString()
        binding.soloActivityCardName.setCardBackgroundColor(user.color)
    }

    private fun initSpinnerSelectParticipant() {

        spinner = binding.soloActivitySpinnerList

        viewModel.users.observe(this) { list ->

            // Create an ArrayAdapter using a simple spinner layout and languages array
            val aa = ArrayAdapter(this, R.layout.spinner_item, list)
            // Set layout to use when the list of choices appear
            aa.setDropDownViewResource(R.layout.spinner_dropdown_item)
            // Set Adapter to Spinner
            spinner.adapter = aa
            spinner.setSelection(spinnerPos)

                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        currentUser = list[position]
                        populateView(list[position])
                        spinnerPos = position
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) { }
                }
            }
    }

    private fun setClickListenerPointView() {
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

    private fun setToolbar() {
        val toolbarBackImg: ImageView = findViewById(R.id.toolbar_image_view_back)
        val toolbarMenu: ImageView = findViewById(R.id.toolbar_image_view_menu)
        val toolbarAdd: ImageView = findViewById(R.id.toolbar_image_view_add)
        val toolbarTitle: TextView = findViewById(R.id.toolbar_title)

        toolbarMenu.isVisible = false
        toolbarTitle.setText(R.string.solo_counter)
        toolbarBackImg.setOnClickListener {
            viewModel.users.observe(this) {
                viewModel.resetAllUsersPoint(it)
            }
            finish()
        }
        toolbarAdd.setOnClickListener { setDisplayAlertDialogParticipant(null) }
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

        // On click button Ok
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

    private fun setOnClickEditIcon() {
        binding.soloActivityImageViewMenu.setOnClickListener {
            val popupMenu = PopupMenu(this, it)
            popupMenu.menu.add("Edit").setOnMenuItemClickListener {
                setDisplayAlertDialogParticipant(currentUser!!)
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("spinner_position", spinnerPos)
    }

}