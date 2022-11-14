package com.example.scorecounter.ui

import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.scorecounter.R
import com.example.scorecounter.data.UserRoomDatabase
import com.example.scorecounter.databinding.ActivityCounterListBinding
import com.example.scorecounter.databinding.ToolbarLayoutBinding
import com.example.scorecounter.databinding.ToolbarLayoutStepBinding
import com.example.scorecounter.model.entity.User
import com.example.scorecounter.repository.Repository
import com.example.scorecounter.utils.OnItemClickListener
import com.example.scorecounter.ui.adapter.ParticipantAdapter
import com.example.scorecounter.ui.dialog.DialogDiceResult
import com.example.scorecounter.ui.dialog.DialogInput
import com.example.scorecounter.ui.dialog.DialogMenu
import com.example.scorecounter.ui.dialog.DialogParticipant
import com.example.scorecounter.utils.EnumDialogInput
import com.example.scorecounter.utils.EnumItem
import com.example.scorecounter.utils.EnumVHSelect
import com.example.scorecounter.viewmodel.SharedViewModel
import com.example.scorecounter.viewmodel.SharedViewModelFactory

class CounterListActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var binding: ActivityCounterListBinding
    private lateinit var stepBinding: ToolbarLayoutStepBinding
    private lateinit var toolbarBinding: ToolbarLayoutBinding
    private lateinit var adapter : ParticipantAdapter
    private lateinit var viewModel: SharedViewModel
    private var step = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P ) {
            window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES }

        binding = ActivityCounterListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        stepBinding = ToolbarLayoutStepBinding.bind(binding.root)
        toolbarBinding = ToolbarLayoutBinding.bind(binding.root)

        // set immersive mode
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, binding.root).let {
            it.hide(WindowInsetsCompat.Type.systemBars())
            it.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        val dao = UserRoomDatabase.getInstance(application).userDao
        val repository = Repository(dao)
        val factory = SharedViewModelFactory(repository)

        viewModel = ViewModelProvider(this, factory)[SharedViewModel::class.java]
        viewModel.step.observe(this) { step = it }

        displayList()
        setToolbar()
    }

    override fun setOnItemClickListener(user: User, enum: EnumItem, pos: Int) {
        when (enum) {
            EnumItem.DELETE -> viewModel.deleteUser(user)
            EnumItem.EDIT -> DialogParticipant(user, viewModel).show(
                supportFragmentManager,
                "dialog_user"
            )
            EnumItem.RESET_POINT -> {
                user.score = 0
                viewModel.updateUser(user)
            }
            EnumItem.ADD_POINT -> {
                user.score += step
                viewModel.updateUser(user)
            }
            EnumItem.REMOVE_POINT -> {
                user.score -= step
                viewModel.updateUser(user)
            }
            EnumItem.SET_POINT -> {
                DialogInput(user, viewModel, EnumDialogInput.SCORE_INPUT).show(supportFragmentManager, "dialog_score")
            }
            else -> {}
        }
    }

    private fun displayList() {
        viewModel.users.observe(this) {
            adapter = ParticipantAdapter(it, this, EnumVHSelect.LIST, null)
            binding.recyclerViewGuests.adapter = adapter

            var column = 1
            if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) { column = 2 }
            binding.recyclerViewGuests.layoutManager = GridLayoutManager( this, column)
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
                startActivity(Intent(this@CounterListActivity, MainActivity::class.java))
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
}