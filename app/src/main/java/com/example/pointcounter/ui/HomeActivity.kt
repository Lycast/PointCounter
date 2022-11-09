package com.example.pointcounter.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.ViewModelProvider
import com.example.pointcounter.R
import com.example.pointcounter.data.UserRoomDatabase
import com.example.pointcounter.databinding.ActivityHomeBinding
import com.example.pointcounter.model.entity.User
import com.example.pointcounter.repository.Repository
import com.example.pointcounter.ui.dialog.DialogParticipant
import com.example.pointcounter.viewmodel.SharedViewModel
import com.example.pointcounter.viewmodel.SharedViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel: SharedViewModel
    private var list = listOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dao = UserRoomDatabase.getInstance(application).userDao
        val repository = Repository(dao)
        val factory = SharedViewModelFactory(repository)

        viewModel = ViewModelProvider(this, factory)[SharedViewModel::class.java]
        viewModel.users.observe(this) { list =it }

        startCounter()
        setOnClickBottomNavigation()
        setToolbar()
    }

    private fun startCounter() {
        binding.imageViewListCard.setOnClickListener {
            startActivity(Intent(this, CounterListActivity::class.java))
        }
        binding.imageViewSoloCard.setOnClickListener {
            startActivity(Intent(this, CounterSoloActivity::class.java))
        }
        binding.imageViewDuoCard.setOnClickListener {
            startActivity(Intent(this, CounterDuoActivity::class.java))
        }
        binding.imageViewCompactListCard.setOnClickListener {
            startActivity(Intent(this, CounterCompactListActivity::class.java))
        }
    }

    private fun setOnClickBottomNavigation() {
        binding.homeActivityBottomNavigation.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.bottom_menu_list -> {
                    // Respond to navigation item 1 click
                    Snackbar.make(binding.root,"action not yet implemented",Snackbar.LENGTH_SHORT).show()
                    true
                }
                R.id.bottom_menu_home -> {
                    // Respond to navigation item 2 click
                    Snackbar.make(binding.root,"action not yet implemented",Snackbar.LENGTH_SHORT).show()
                    true
                }
                R.id.bottom_menu_stats -> {
                    // Respond to navigation item 2 click
                    Snackbar.make(binding.root,"action not yet implemented",Snackbar.LENGTH_SHORT).show()
                    true
                }
                else -> {false}
            }
        }
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
            popupMenu.menu.add("Delete all counters").setOnMenuItemClickListener {
                viewModel.deleteAllUsers()
                true
            }
            popupMenu.menu.add("Reset all counters").setOnMenuItemClickListener {
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