package com.example.pointcounter.ui

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
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import com.example.pointcounter.R
import com.example.pointcounter.data.UserRoomDatabase
import com.example.pointcounter.databinding.ActivityCounterSoloBinding
import com.example.pointcounter.model.entity.User
import com.example.pointcounter.repository.Repository
import com.example.pointcounter.ui.dialog.DialogParticipant
import com.example.pointcounter.viewmodel.SharedViewModel
import com.example.pointcounter.viewmodel.SharedViewModelFactory

class CounterSoloActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCounterSoloBinding
    private lateinit var viewModel: SharedViewModel
    private lateinit var spinner: Spinner
    private var currentUser: User? = null
    private var spinnerPos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P ) {
            window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES }

        binding = ActivityCounterSoloBinding.inflate(layoutInflater)
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

        initSpinnerSelectParticipant()
        setClickListenerPointView()
        setOnClickEditIcon()

        if (savedInstanceState != null) {
            spinnerPos = savedInstanceState.getInt("spinner_position", 0)
        }
    }

    private fun populateView(user: User) {
        binding.soloActivityTextViewScore.text = user.score.toString()
        binding.soloActivityCardName.setCardBackgroundColor(user.color)
    }

    private fun initSpinnerSelectParticipant() {

        spinner = binding.soloActivitySpinnerList

        viewModel.users.observe(this) { list ->

            if (list.isEmpty()) viewModel.addUser( User(0, "Guest", 0, viewModel.getRandomColor())) // change that by list default

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
        binding.soloActivityImageViewAddPoint.setOnClickListener {
            currentUser!!.score++
            viewModel.updateUser(currentUser!!)
        }
        binding.soloActivityImageViewAddPoint.setOnLongClickListener {
            currentUser!!.score+=10
            viewModel.updateUser(currentUser!!)
            return@setOnLongClickListener true
        }

        binding.soloActivityImageViewRemovePoint.setOnClickListener {
            currentUser!!.score--
            viewModel.updateUser(currentUser!!)
        }
        binding.soloActivityImageViewRemovePoint.setOnLongClickListener {
            currentUser!!.score -= 10
            viewModel.updateUser(currentUser!!)
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
                currentUser!!.score = 0
                viewModel.updateUser(currentUser!!)
                true
            }
            popupMenu.menu.add("Delete").setOnMenuItemClickListener {
                viewModel.deleteUser(currentUser!!)
                true
            }
            popupMenu.show()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("spinner_position", spinnerPos)
    }
}