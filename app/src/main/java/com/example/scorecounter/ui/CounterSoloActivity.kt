package com.example.scorecounter.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import com.example.scorecounter.R
import com.example.scorecounter.databinding.ActivityCounterSoloBinding
import com.example.scorecounter.databinding.ToolbarLayoutBinding
import com.example.scorecounter.databinding.ToolbarLayoutStepBinding
import com.example.scorecounter.data.UserRoomDatabase
import com.example.scorecounter.model.entity.User
import com.example.scorecounter.repository.Repository
import com.example.scorecounter.ui.dialog.DialogDiceResult
import com.example.scorecounter.ui.dialog.DialogInput
import com.example.scorecounter.ui.dialog.DialogMenu
import com.example.scorecounter.ui.dialog.DialogParticipant
import com.example.scorecounter.utils.EnumDialogInput
import com.example.scorecounter.viewmodel.SharedViewModel
import com.example.scorecounter.viewmodel.SharedViewModelFactory

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
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }

        binding = ActivityCounterSoloBinding.inflate(layoutInflater)
        setContentView(binding.root)
        stepBinding = ToolbarLayoutStepBinding.bind(binding.root)
        toolbarBinding = ToolbarLayoutBinding.bind(binding.root)

        if (savedInstanceState != null) {
            spinnerPos = savedInstanceState.getInt("spinner_position", 0)
        }

        // set immersive mode
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, binding.root).let {
            it.hide(WindowInsetsCompat.Type.systemBars())
            it.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
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
        binding.tvScore.text = user.score.toString()
        binding.soloCard.setCardBackgroundColor(user.color)
    }

    private fun initSpinnerSelectParticipant() {

        spinner = binding.spinnerParticipant

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
        binding.soloCard.setOnLongClickListener {
            DialogInput(currentUser, viewModel, EnumDialogInput.SCORE_INPUT).show(supportFragmentManager, "dialog_score")
            return@setOnLongClickListener true
        }
    }

    private fun setOnClickEditIcon() {
        binding.ivEdit.setOnClickListener {
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
            stepBinding.stepSetup.setBackgroundColor(ContextCompat.getColor(this, R.color.opacity_0))
            stepBinding.stepSetup.setText(R.string.step)

            when(it) {
                1 -> stepBinding.step1.setBackgroundColor(ContextCompat.getColor(this, R.color.light_gray))
                5 -> stepBinding.step5.setBackgroundColor(ContextCompat.getColor(this, R.color.light_gray))
                10 -> stepBinding.step10.setBackgroundColor(ContextCompat.getColor(this, R.color.light_gray))
                25 -> stepBinding.step25.setBackgroundColor(ContextCompat.getColor(this, R.color.light_gray))
                else -> {
                    stepBinding.stepSetup.setBackgroundColor(ContextCompat.getColor(this, R.color.light_gray))
                    stepBinding.stepSetup.text = it.toString()
                }
            }
        }

        toolbarBinding.apply {
            toolbarImageAdd.setOnClickListener { viewModel.addUser(User(0,viewModel.getRndName(), 0, viewModel.getRndColor())) }
            toolbarImageViewDice.setOnClickListener {
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
            stepSetup.setOnClickListener { DialogInput(null, viewModel, EnumDialogInput.STEP_INPUT).show(supportFragmentManager, "dialog_step") }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("spinner_position", spinnerPos)
    }
}