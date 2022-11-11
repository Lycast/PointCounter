package com.example.pointcounter.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.pointcounter.R
import com.example.pointcounter.data.UserRoomDatabase
import com.example.pointcounter.databinding.ActivityCounterSoloBinding
import com.example.pointcounter.databinding.ToolbarLayoutBinding
import com.example.pointcounter.databinding.ToolbarLayoutStepBinding
import com.example.pointcounter.model.entity.User
import com.example.pointcounter.repository.Repository
import com.example.pointcounter.ui.dialog.DialogDiceResult
import com.example.pointcounter.ui.dialog.DialogInputScore
import com.example.pointcounter.ui.dialog.DialogMenu
import com.example.pointcounter.ui.dialog.DialogParticipant
import com.example.pointcounter.utils.EnumDialogEditText
import com.example.pointcounter.viewmodel.SharedViewModel
import com.example.pointcounter.viewmodel.SharedViewModelFactory

class CounterSoloActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCounterSoloBinding
    private lateinit var stepBinding: ToolbarLayoutStepBinding
    private lateinit var toolbarBinding: ToolbarLayoutBinding
    private lateinit var viewModel: SharedViewModel
    private lateinit var spinner: Spinner
    private lateinit var currentUser: User
    private var spinnerPos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCounterSoloBinding.inflate(layoutInflater)
        setContentView(binding.root)
        stepBinding = ToolbarLayoutStepBinding.bind(binding.root)
        toolbarBinding = ToolbarLayoutBinding.bind(binding.root)

        if (savedInstanceState != null) {
            spinnerPos = savedInstanceState.getInt("spinner_position", 0)
        }

        val dao = UserRoomDatabase.getInstance(application).userDao
        val repository = Repository(dao)
        val factory = SharedViewModelFactory(repository)

        viewModel = ViewModelProvider(this, factory)[SharedViewModel::class.java]

        initSpinnerSelectParticipant()
        setClickListenerPointView()
        setOnClickEditIcon()
        setToolbar()
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
        viewModel.step.observe(this) { step ->
            binding.soloActivityImageViewAddPoint.setOnClickListener {
                currentUser.score += step
                viewModel.updateUser(currentUser)
            }

            binding.soloActivityImageViewRemovePoint.setOnClickListener {
                currentUser.score -= step
                viewModel.updateUser(currentUser)
            }
        }
        binding.layoutScore.setOnLongClickListener {
            DialogInputScore(currentUser, viewModel, EnumDialogEditText.SCORE_INPUT).show(supportFragmentManager, "dialog_score")
            return@setOnLongClickListener true
        }
    }

    private fun setOnClickEditIcon() {
        binding.soloActivityImageViewMenu.setOnClickListener {
            val popupMenu = PopupMenu(this, it)
            popupMenu.menu.add("Edit").setOnMenuItemClickListener {
                DialogParticipant(currentUser, viewModel).show(supportFragmentManager, "dialog_user")
                true
            }
            popupMenu.menu.add("Reset Counter").setOnMenuItemClickListener {
                currentUser.score = 0
                viewModel.updateUser(currentUser)
                true
            }
            popupMenu.show()
        }
    }

    private fun setToolbar() {

        viewModel.step.observe(this) {
            stepBinding.step1.setBackgroundColor(ContextCompat.getColor(this, R.color.opacity_0))
            stepBinding.step5.setBackgroundColor(ContextCompat.getColor(this, R.color.opacity_0))
            stepBinding.step10.setBackgroundColor(ContextCompat.getColor(this, R.color.opacity_0))
            stepBinding.step25.setBackgroundColor(ContextCompat.getColor(this, R.color.opacity_0))
            stepBinding.step50.setBackgroundColor(ContextCompat.getColor(this, R.color.opacity_0))

            if (it == 1 ) stepBinding.step1.setBackgroundColor(ContextCompat.getColor(this, R.color.light_gray))
            if (it == 5 ) stepBinding.step5.setBackgroundColor(ContextCompat.getColor(this, R.color.light_gray))
            if (it == 10 ) stepBinding.step10.setBackgroundColor(ContextCompat.getColor(this, R.color.light_gray))
            if (it == 25 ) stepBinding.step25.setBackgroundColor(ContextCompat.getColor(this, R.color.light_gray))
            if (it == 50 ) stepBinding.step50.setBackgroundColor(ContextCompat.getColor(this, R.color.light_gray))
        }

        toolbarBinding.apply {
            toolbarImageAdd.setOnClickListener { viewModel.addUser(User(0,"Guest", 0, viewModel.getRandomColor())) }
            toolbarImageViewDice.setOnClickListener {
                viewModel.launchDice()
                DialogDiceResult(viewModel).show(supportFragmentManager, "dialog_dice")
            }
            toolbarImageViewBack.setOnClickListener {
                startActivity(Intent(this@CounterSoloActivity, MainActivity::class.java))
                finish()
            }
            toolbarImageViewMenu.setOnClickListener {
                DialogMenu(viewModel).show(supportFragmentManager, "dialog_menu")
            }
        }

        stepBinding.apply {
            step1.setOnClickListener { viewModel.setStep(1) }
            step5.setOnClickListener { viewModel.setStep(5) }
            step10.setOnClickListener { viewModel.setStep(10) }
            step25.setOnClickListener { viewModel.setStep(25) }
            step50.setOnClickListener { viewModel.setStep(50) }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("spinner_position", spinnerPos)
    }
}