package com.example.pointcounter.ui

import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import com.example.pointcounter.R
import com.example.pointcounter.data.UserRoomDatabase
import com.example.pointcounter.databinding.ActivityCounterDuoBinding
import com.example.pointcounter.databinding.ToolbarLayoutBinding
import com.example.pointcounter.databinding.ToolbarLayoutStepBinding
import com.example.pointcounter.model.entity.User
import com.example.pointcounter.repository.Repository
import com.example.pointcounter.ui.dialog.DialogDiceResult
import com.example.pointcounter.ui.dialog.DialogInput
import com.example.pointcounter.ui.dialog.DialogMenu
import com.example.pointcounter.ui.dialog.DialogParticipant
import com.example.pointcounter.utils.EnumDialogEditText
import com.example.pointcounter.viewmodel.SharedViewModel
import com.example.pointcounter.viewmodel.SharedViewModelFactory

class CounterDuoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCounterDuoBinding
    private lateinit var stepBinding: ToolbarLayoutStepBinding
    private lateinit var toolbarBinding: ToolbarLayoutBinding
    private lateinit var viewModel: SharedViewModel
    private lateinit var spinnerA: Spinner
    private lateinit var spinnerB: Spinner
    private var spinnerAPos = 0
    private var spinnerBPos = 1
    private lateinit var currentParticipantA: User
    private lateinit var currentParticipantB: User


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }

        binding = ActivityCounterDuoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        stepBinding = ToolbarLayoutStepBinding.bind(binding.root)
        toolbarBinding = ToolbarLayoutBinding.bind(binding.root)

        if (savedInstanceState != null) {
            spinnerAPos = savedInstanceState.getInt("spinner_a_position", 0)
            spinnerBPos = savedInstanceState.getInt("spinner_b_position", 0)
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

        if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            binding.duoLinearLayout.orientation = LinearLayout.HORIZONTAL

        initSpinnerSelectParticipantA()
        initSpinnerSelectParticipantB()
        setClickListenerPointView()
        setOnClickEditIcon()
        setToolbar()
    }

    private fun populateViewParticipantA(user: User) {
        binding.duoTextViewScore.text = user.score.toString()
        binding.duoCounter1.setCardBackgroundColor(user.color)
    }

    private fun populateViewParticipantB(user: User) {
        binding.duoTextViewScore2.text = user.score.toString()
        binding.duoCounter2.setCardBackgroundColor(user.color)
    }

    private fun initSpinnerSelectParticipantA() {
        viewModel.users.observe(this) {

            spinnerA = binding.duoSpinnerList

            val aa = ArrayAdapter(this, R.layout.spinner_item_duo, it)
            aa.setDropDownViewResource(R.layout.spinner_dropdown_item)
            spinnerA.adapter = aa
            spinnerA.setSelection(spinnerAPos)

            spinnerA.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
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

            spinnerB = binding.duoSpinnerList2
            val aa = ArrayAdapter(this, R.layout.spinner_item_duo, it)
            aa.setDropDownViewResource(R.layout.spinner_dropdown_item)
            spinnerB.adapter = aa
            spinnerB.setSelection(spinnerBPos)

            spinnerB.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    populateViewParticipantB(it[position])
                    currentParticipantB = it[position]
                    spinnerBPos = position
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }
    }

    private fun setClickListenerPointView() {
        viewModel.step.observe(this) { step ->
            // Add point participant A
            binding.duoCardAddPoint.setOnClickListener {
                currentParticipantA.score += step
                viewModel.updateUser(currentParticipantA)
            }

            // Add point participant B
            binding.duoCardAddPoint2.setOnClickListener {
                currentParticipantB.score += step
                viewModel.updateUser(currentParticipantB)
            }

            // Remove point participant A
            binding.duoCardRemovePoint.setOnClickListener {
                currentParticipantA.score -= step
                viewModel.updateUser(currentParticipantA)
            }

            // Remove point participant B
            binding.duoCardRemovePoint2.setOnClickListener {
                currentParticipantB.score -= step
                viewModel.updateUser(currentParticipantB)
            }
        }
    }


    private fun setOnClickEditIcon() {
        // Set edit click participant A
        binding.duoImageViewEdit.setOnClickListener {
            val popupMenu = PopupMenu(this, it)
            popupMenu.menu.add("Edit").setOnMenuItemClickListener {
                DialogParticipant(currentParticipantA, viewModel).show(
                    supportFragmentManager,
                    "dialog_user"
                )
                true
            }
            popupMenu.menu.add("Reset Counter").setOnMenuItemClickListener {
                currentParticipantA.score = 0
                viewModel.updateUser(currentParticipantA)
                true
            }
            popupMenu.show()
        }

        // Set edit click participant B
        binding.duoImageViewEdit.setOnClickListener {
            val popupMenu = PopupMenu(this, it)
            popupMenu.menu.add("Edit").setOnMenuItemClickListener {
                DialogParticipant(currentParticipantB, viewModel).show(supportFragmentManager, "dialog_user")
                true
            }
            popupMenu.menu.add("Reset Counter").setOnMenuItemClickListener {
                currentParticipantB.score = 0
                viewModel.updateUser(currentParticipantB)
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
            toolbarImageAdd.setOnClickListener { viewModel.addUser(User(0,"Guest", 0, viewModel.getRandomColor())) }
            toolbarImageViewDice.setOnClickListener {
                viewModel.launchDice()
                DialogDiceResult(viewModel).show(supportFragmentManager, "dialog_dice")
            }
            toolbarImageViewBack.setOnClickListener {
                startActivity(Intent(this@CounterDuoActivity, MainActivity::class.java))
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
            stepSetup.setOnClickListener { DialogInput(null, viewModel, EnumDialogEditText.STEP_INPUT).show(supportFragmentManager, "dialog_step") }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("spinner_a_position", spinnerAPos)
        outState.putInt("spinner_b_position", spinnerBPos)
    }
}