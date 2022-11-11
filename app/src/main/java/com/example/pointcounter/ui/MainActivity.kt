package com.example.pointcounter.ui

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pointcounter.R
import com.example.pointcounter.data.UserRoomDatabase
import com.example.pointcounter.databinding.ActivityMainBinding
import com.example.pointcounter.model.entity.User
import com.example.pointcounter.repository.Repository
import com.example.pointcounter.ui.adapter.MainViewPagerAdapter
import com.example.pointcounter.ui.dialog.DialogDiceResult
import com.example.pointcounter.ui.dialog.DialogMenu
import com.example.pointcounter.ui.navigation.HomeFragment
import com.example.pointcounter.ui.navigation.ListFragment
import com.example.pointcounter.ui.navigation.StatsFragment
import com.example.pointcounter.viewmodel.SharedViewModel
import com.example.pointcounter.viewmodel.SharedViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: SharedViewModel
    private val tabsList = listOf(R.drawable._home_24, R.drawable._list_24, R.drawable._stats_24)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        val fragments: ArrayList<Fragment> = arrayListOf(HomeFragment(viewModel), ListFragment(), StatsFragment())
        val viewPagerAdapter = MainViewPagerAdapter(fragments, this)
        binding.mainViewPager.adapter = viewPagerAdapter
        TabLayoutMediator(binding.mainTabLayout, binding.mainViewPager) {
                tab, position -> tab.icon = ContextCompat.getDrawable(this, tabsList[position])
        }.attach()
    }

    private fun setToolbar() {
        val toolbarBackImg: ImageView = findViewById(R.id.toolbar_image_view_back)
        val toolbarMenu: ImageView = findViewById(R.id.toolbar_image_view_menu)
        val toolbarDiceImg: ImageView = findViewById(R.id.toolbar_image_view_dice)
        val toolbarAdd: ImageView = findViewById(R.id.toolbar_image_add)

        toolbarAdd.setOnClickListener {
            viewModel.addUser(User(0,"Guest", 0, viewModel.getRandomColor()))
        }


        toolbarDiceImg.setOnClickListener {
            viewModel.launchDice()
            DialogDiceResult(viewModel).show(supportFragmentManager, "dialog_dice")
        }

        toolbarBackImg.setOnClickListener { finish() }

        toolbarMenu.setOnClickListener {
            DialogMenu(viewModel).show(supportFragmentManager, "dialog_menu")
        }
    }
}