package com.example.pointcounter.ui

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
import com.example.pointcounter.R
import com.example.pointcounter.data.UserRoomDatabase
import com.example.pointcounter.databinding.ActivityCounterListBinding
import com.example.pointcounter.databinding.ToolbarLayoutBinding
import com.example.pointcounter.databinding.ToolbarLayoutStepBinding
import com.example.pointcounter.model.entity.User
import com.example.pointcounter.repository.Repository
import com.example.pointcounter.utils.OnItemClickListener
import com.example.pointcounter.ui.adapter.ParticipantAdapter
import com.example.pointcounter.ui.dialog.DialogDiceResult
import com.example.pointcounter.ui.dialog.DialogInput
import com.example.pointcounter.ui.dialog.DialogMenu
import com.example.pointcounter.ui.dialog.DialogParticipant
import com.example.pointcounter.utils.EnumDialogInput
import com.example.pointcounter.utils.EnumItem
import com.example.pointcounter.utils.EnumVHSelect
import com.example.pointcounter.viewmodel.SharedViewModel
import com.example.pointcounter.viewmodel.SharedViewModelFactory

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