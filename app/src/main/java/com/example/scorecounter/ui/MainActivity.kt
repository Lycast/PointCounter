package com.example.scorecounter.ui

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.scorecounter.R
import com.example.scorecounter.data.UserRoomDatabase
import com.example.scorecounter.databinding.ActivityMainBinding
import com.example.scorecounter.databinding.ToolbarLayoutBinding
import com.example.scorecounter.model.entity.User
import com.example.scorecounter.repository.Repository
import com.example.scorecounter.ui.adapter.viewpager.MainViewPagerAdapter
import com.example.scorecounter.ui.dialog.DialogDiceResult
import com.example.scorecounter.ui.dialog.DialogInputStep
import com.example.scorecounter.ui.dialog.DialogMenu
import com.example.scorecounter.ui.navigation.CounterFragment
import com.example.scorecounter.ui.navigation.GameFragment
import com.example.scorecounter.ui.navigation.ContactFragment
import com.example.scorecounter.ui.navigation.ListFragment
import com.example.scorecounter.viewmodel.SharedViewModel
import com.example.scorecounter.viewmodel.SharedViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toolbarBinding: ToolbarLayoutBinding
    private lateinit var viewModel: SharedViewModel
    private val tabsList = listOf(
        R.drawable.svg_counter,
        R.drawable.playlist_add_24,
        R.drawable.crown_24,
        R.drawable.info_24
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set immersive mode
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, binding.root).let {
            it.hide(WindowInsetsCompat.Type.systemBars())
            it.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.mainTabLayout.visibility = View.GONE
        } else binding.mainTabLayout.visibility = View.VISIBLE

        toolbarBinding = ToolbarLayoutBinding.bind(binding.root)
        initViewModel()
        initViewPager()
        setToolbar()
    }

    private fun initViewModel() {
        val dao = UserRoomDatabase.getInstance(application).userDao
        val repository = Repository(dao)
        val factory = SharedViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[SharedViewModel::class.java]
    }

    private fun initViewPager() {
        val fragments: ArrayList<Fragment> =
            arrayListOf(CounterFragment(), ListFragment(), GameFragment(), ContactFragment())
        val viewPagerAdapter = MainViewPagerAdapter(fragments, this)
        binding.mainViewPager.adapter = viewPagerAdapter
        TabLayoutMediator(binding.mainTabLayout, binding.mainViewPager) { tab, position ->
            tab.icon = ContextCompat.getDrawable(this, tabsList[position])
        }.attach()
    }

    private fun setToolbar() {
        toolbarBinding.apply {

            viewModel.step.observe(this@MainActivity) { tvStep.text = it.toString() }
            toolbarImageAdd.setOnClickListener { viewModel.addUser(User(0, viewModel.getRndName(), 0, viewModel.getRndColor())) }
            toolbarImageViewDice.setOnClickListener { DialogDiceResult().show(supportFragmentManager, "dialog_dice") }
            toolbarImageViewMenu.setOnClickListener { DialogMenu().show(supportFragmentManager, "dialog_menu") }

            tvStep.setOnClickListener {
                var listOfStep = listOf<Int>()

                viewModel.listOfStep.observe(this@MainActivity) { list -> listOfStep = list }

                val popupMenu = PopupMenu(this@MainActivity, it)

                popupMenu.menu.add(listOfStep[0].toString()).setOnMenuItemClickListener {
                    viewModel.setStep(listOfStep[0])
                    true
                }
                popupMenu.menu.add(listOfStep[1].toString()).setOnMenuItemClickListener {
                    viewModel.setStep(listOfStep[1])
                    true
                }
                popupMenu.menu.add(listOfStep[2].toString()).setOnMenuItemClickListener {
                    viewModel.setStep(listOfStep[2])
                    true
                }
                popupMenu.menu.add(listOfStep[3].toString()).setOnMenuItemClickListener {
                    viewModel.setStep(listOfStep[3])
                    true
                }
                popupMenu.menu.add("Edit").setOnMenuItemClickListener {
                    DialogInputStep().show(supportFragmentManager, "dialog_step")
                    true
                }
                popupMenu.show()
            }

            tvStep.setOnLongClickListener {
                DialogInputStep().show(supportFragmentManager, "dialog_step")
                return@setOnLongClickListener true
            }
        }
    }
}