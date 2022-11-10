package com.example.pointcounter.ui

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pointcounter.data.UserRoomDatabase
import com.example.pointcounter.databinding.ActivityCounterCompactListBinding
import com.example.pointcounter.model.entity.User
import com.example.pointcounter.repository.Repository
import com.example.pointcounter.utils.OnItemClickListener
import com.example.pointcounter.ui.adapter.UserAdapter
import com.example.pointcounter.ui.dialog.DialogParticipant
import com.example.pointcounter.utils.UserEnum
import com.example.pointcounter.utils.ViewHolderEnum
import com.example.pointcounter.viewmodel.SharedViewModel
import com.example.pointcounter.viewmodel.SharedViewModelFactory

class CounterCompactListActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var binding: ActivityCounterCompactListBinding
    private lateinit var adapter : UserAdapter
    private lateinit var viewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P ) {
            window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES }

        binding = ActivityCounterCompactListBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        displayList()
    }

    override fun setOnItemClickListener(user: User, enum: UserEnum) {
        when ( enum ) {
            UserEnum.DELETE -> viewModel.deleteUser(user)
            UserEnum.EDIT -> DialogParticipant(user, viewModel).show(supportFragmentManager, "dialog_user")
            UserEnum.RESET_POINT -> {
                user.score = 0
                viewModel.updateUser(user)
            }
            UserEnum.ADD_1_POINT -> {
                user.score ++
                viewModel.updateUser(user)
            }
            UserEnum.ADD_10_POINT -> {
                user.score += 10
                viewModel.updateUser(user)
            }
            UserEnum.REMOVE_1_POINT -> {
                user.score --
                viewModel.updateUser(user)
            }
            UserEnum.REMOVE_10_POINT -> {
                user.score -= 10
                viewModel.updateUser(user)
            }
        }
    }

    private fun displayList() {
        viewModel.users.observe(this) {
            adapter = UserAdapter(it, this, ViewHolderEnum.COMPACT_LIST)
            binding.recyclerViewGuests.adapter = adapter

            if (it.isEmpty())
                for (i in 1..20) {
                    viewModel.addUser( User(0, "Guest", 0, viewModel.getRandomColor())) // change that by list default
                }

            var column = 2
            if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) { column = 4 }
            binding.recyclerViewGuests.layoutManager = GridLayoutManager( this, column)
        }
    }
}