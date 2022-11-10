package com.example.pointcounter.ui

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pointcounter.R
import com.example.pointcounter.data.UserRoomDatabase
import com.example.pointcounter.databinding.ActivityMainBinding
import com.example.pointcounter.model.entity.User
import com.example.pointcounter.repository.Repository
import com.example.pointcounter.ui.adapter.MainViewPagerAdapter
import com.example.pointcounter.ui.navigation.HomeFragment
import com.example.pointcounter.ui.navigation.ListFragment
import com.example.pointcounter.ui.navigation.StatsFragment
import com.example.pointcounter.ui.dialog.DialogParticipant
import com.example.pointcounter.viewmodel.SharedViewModel
import com.example.pointcounter.viewmodel.SharedViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: SharedViewModel
    private var list = listOf<User>()
    private val fragments: ArrayList<Fragment> = arrayListOf(HomeFragment(), ListFragment(), StatsFragment())
    private val viewPagerAdapter = MainViewPagerAdapter(fragments, this)
    private val tabsList = listOf(R.drawable._home_24, R.drawable._list_24, R.drawable._stats_24)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dao = UserRoomDatabase.getInstance(application).userDao
        val repository = Repository(dao)
        val factory = SharedViewModelFactory(repository)

        binding.mainViewPager.adapter = viewPagerAdapter
        TabLayoutMediator(binding.mainTabLayout, binding.mainViewPager) {
            tab, position -> tab.icon = ContextCompat.getDrawable(this, tabsList[position])
        }.attach()

        viewModel = ViewModelProvider(this, factory)[SharedViewModel::class.java]
        viewModel.users.observe(this) { list = it }

        setToolbar()
    }

    private fun setToolbar() {
        val toolbarBackImg: ImageView = findViewById(R.id.toolbar_image_view_back)
        val toolbarMenu: ImageView = findViewById(R.id.toolbar_image_view_menu)
        val toolbarAdd: ImageView = findViewById(R.id.toolbar_image_view_add)

        toolbarBackImg.setOnClickListener {
            finish()
        }
        toolbarAdd.setOnClickListener { DialogParticipant(null, viewModel).show(supportFragmentManager, "dialog_user") }
        toolbarMenu.setOnClickListener {
            val popupMenu = PopupMenu(this, it)
            popupMenu.menu.add("Delete all participants").setOnMenuItemClickListener {
                viewModel.deleteAllUsers()
                true
            }
            popupMenu.menu.add("Reset all score").setOnMenuItemClickListener {
                resetAllPoints()
                true
            }
            popupMenu.show()
        }
    }

    private fun resetAllPoints() = CoroutineScope(Dispatchers.IO).launch {
        suspend {
            viewModel.resetAllUsersPoint(list)
        }.invoke()
    }

}