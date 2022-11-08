package com.example.pointcounter.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.pointcounter.R
import com.example.pointcounter.data.UserRoomDatabase
import com.example.pointcounter.databinding.ActivityCounterDuoBinding
import com.example.pointcounter.databinding.AlertDialogParticipantBinding
import com.example.pointcounter.model.entity.User
import com.example.pointcounter.repository.Repository
import com.example.pointcounter.viewmodel.SharedViewModel
import com.example.pointcounter.viewmodel.SharedViewModelFactory
import com.google.android.material.snackbar.Snackbar

class CounterDuoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCounterDuoBinding
    private lateinit var viewModel: SharedViewModel
    private lateinit var spinnerA: Spinner
    private lateinit var spinnerB: Spinner
    private var spinnerAPos = 0
    private var spinnerBPos = 1
    private var currentParticipantA: User? = null
    private var currentParticipantB: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCounterDuoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState != null) {
            spinnerAPos = savedInstanceState.getInt("spinner_a_position", 0)
            spinnerBPos = savedInstanceState.getInt("spinner_b_position", 0)
        }

        val dao = UserRoomDatabase.getInstance(application).userDao
        val repository = Repository(dao)
        val factory = SharedViewModelFactory(repository)

        viewModel = ViewModelProvider(this, factory)[SharedViewModel::class.java]

        initSpinnerSelectParticipantA()
        initSpinnerSelectParticipantB()
        setClickListenerPointView()
        setToolbar()
        setOnClickEditIcon()
    }

    private fun populateViewParticipantA(user: User) {
        binding.duoActivityTextViewScore1.text = user.score.toString()
        binding.counter1.setBackgroundColor(user.color)
    }

    private fun populateViewParticipantB(user: User) {
        binding.duoActivityTextViewScore2.text = user.score.toString()
        binding.counter2.setBackgroundColor(user.color)
    }

    private fun initSpinnerSelectParticipantA() {
        viewModel.users.observe(this) {

            spinnerA = binding.duoActivitySpinnerList1

            val aa = ArrayAdapter(this, R.layout.spinner_item_duo, it)
            aa.setDropDownViewResource(R.layout.spinner_dropdown_item)
            spinnerA.adapter = aa
            spinnerA.setSelection(spinnerAPos)

            spinnerA.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long ) {
                    populateViewParticipantA(it[position])
                    currentParticipantA = it[position]
                    spinnerAPos = position
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }
    }

    private fun initSpinnerSelectParticipantB() {
        viewModel.users.observe(this) {

            spinnerB = binding.duoActivitySpinnerList2

            val aa = ArrayAdapter(this, R.layout.spinner_item_duo, it)
            aa.setDropDownViewResource(R.layout.spinner_dropdown_item)
            spinnerB.adapter = aa
            spinnerB.setSelection(spinnerBPos)

            spinnerB.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long ) {
                    populateViewParticipantB(it[position])
                    currentParticipantB = it[position]
                    spinnerBPos = position
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }
    }

    private fun setClickListenerPointView() {
        // Add point participant A
        binding.duoActivityImageViewAddPoint1.setOnClickListener {
            currentParticipantA!!.score++
            viewModel.updateUser(currentParticipantA!!)
        }
        binding.duoActivityImageViewAddPoint1.setOnLongClickListener {
            currentParticipantA!!.score+=10
            viewModel.updateUser(currentParticipantA!!)
            return@setOnLongClickListener true
        }

        // Add point participant B
        binding.duoActivityImageViewAddPoint2.setOnClickListener {
            currentParticipantB!!.score++
            viewModel.updateUser(currentParticipantB!!)
        }
        binding.duoActivityImageViewAddPoint2.setOnLongClickListener {
            currentParticipantB!!.score+=10
            viewModel.updateUser(currentParticipantB!!)
            return@setOnLongClickListener true
        }

        // Remove point participant A
        binding.duoActivityImageViewRemovePoint1.setOnClickListener {
            currentParticipantA!!.score--
            viewModel.updateUser(currentParticipantA!!)
        }
        binding.duoActivityImageViewRemovePoint1.setOnLongClickListener {
            currentParticipantA!!.score -= 10
            viewModel.updateUser(currentParticipantA!!)
            return@setOnLongClickListener true
        }

        // Remove point participant B
        binding.duoActivityImageViewRemovePoint2.setOnClickListener {
            currentParticipantB!!.score--
            viewModel.updateUser(currentParticipantB!!)
        }
        binding.duoActivityImageViewRemovePoint2.setOnLongClickListener {
            currentParticipantB!!.score -= 10
            viewModel.updateUser(currentParticipantB!!)
            return@setOnLongClickListener true
        }
    }

    private fun setToolbar() {
        val toolbarBackImg: ImageView = findViewById(R.id.toolbar_image_view_back)
        val toolbarMenu: ImageView = findViewById(R.id.toolbar_image_view_menu)
        val toolbarAdd: ImageView = findViewById(R.id.toolbar_image_view_add)
        val toolbarTitle: TextView = findViewById(R.id.toolbar_title)

        toolbarMenu.isVisible = false
        toolbarTitle.setText(R.string.duo_counter)
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
                viewModel.users.observe(this) {
                    spinnerA.setSelection(it.size -1)
                }
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
        // Set edit click participant A
        binding.duoActivityImageViewMenu1.setOnClickListener {
            val popupMenu = PopupMenu(this, it)
            popupMenu.menu.add("Edit").setOnMenuItemClickListener {
                setDisplayAlertDialogParticipant(currentParticipantA!!)
                true
            }
            popupMenu.menu.add("Reset Counter").setOnMenuItemClickListener {
                currentParticipantA!!.score = 0
                viewModel.updateUser(currentParticipantA!!)
                true
            }
            popupMenu.menu.add("Delete").setOnMenuItemClickListener {
                viewModel.deleteUser(currentParticipantA!!)
                true
            }
            popupMenu.show()
        }

        // Set edit click participant B
        binding.duoActivityImageViewMenu2.setOnClickListener {
            val popupMenu = PopupMenu(this, it)
            popupMenu.menu.add("Edit").setOnMenuItemClickListener {
                setDisplayAlertDialogParticipant(currentParticipantB!!)
                true
            }
            popupMenu.menu.add("Reset Counter").setOnMenuItemClickListener {
                currentParticipantB!!.score = 0
                viewModel.updateUser(currentParticipantB!!)
                true
            }
            popupMenu.menu.add("Delete").setOnMenuItemClickListener {
                viewModel.deleteUser(currentParticipantB!!)
                true
            }
            popupMenu.show()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("spinner_a_position", spinnerAPos)
        outState.putInt("spinner_b_position", spinnerBPos)
    }
}