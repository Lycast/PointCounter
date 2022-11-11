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
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import com.example.pointcounter.R
import com.example.pointcounter.data.UserRoomDatabase
import com.example.pointcounter.databinding.ActivityCounterDuoBinding
import com.example.pointcounter.model.entity.User
import com.example.pointcounter.repository.Repository
import com.example.pointcounter.ui.dialog.DialogDiceResult
import com.example.pointcounter.ui.dialog.DialogMenu
import com.example.pointcounter.ui.dialog.DialogParticipant
import com.example.pointcounter.viewmodel.SharedViewModel
import com.example.pointcounter.viewmodel.SharedViewModelFactory

class CounterDuoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCounterDuoBinding
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
        binding.duoActivityTextViewScore1.text = user.score.toString()
        binding.counter1.setBackgroundColor(user.color)
    }

    private fun populateViewParticipantB(user: User) {
        binding.duoActivityTextViewScore2.text = user.score.toString()
        binding.counter2.setBackgroundColor(user.color)
    }

    private fun initSpinnerSelectParticipantA() {
        viewModel.users.observe(this) {

            spinnerA = binding.duoActivitySpinnerList1

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

            spinnerB = binding.duoActivitySpinnerList2

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
        // Add point participant A
        binding.duoActivityImageViewAddPoint1.setOnClickListener {
            currentParticipantA.score++
            viewModel.updateUser(currentParticipantA)
        }
        binding.duoActivityImageViewAddPoint1.setOnLongClickListener {
            currentParticipantA.score += 10
            viewModel.updateUser(currentParticipantA)
            return@setOnLongClickListener true
        }

        // Add point participant B
        binding.duoActivityImageViewAddPoint2.setOnClickListener {
            currentParticipantB.score++
            viewModel.updateUser(currentParticipantB)
        }
        binding.duoActivityImageViewAddPoint2.setOnLongClickListener {
            currentParticipantB.score += 10
            viewModel.updateUser(currentParticipantB)
            return@setOnLongClickListener true
        }

        // Remove point participant A
        binding.duoActivityImageViewRemovePoint1.setOnClickListener {
            currentParticipantA.score--
            viewModel.updateUser(currentParticipantA)
        }
        binding.duoActivityImageViewRemovePoint1.setOnLongClickListener {
            currentParticipantA.score -= 10
            viewModel.updateUser(currentParticipantA)
            return@setOnLongClickListener true
        }

        // Remove point participant B
        binding.duoActivityImageViewRemovePoint2.setOnClickListener {
            currentParticipantB.score--
            viewModel.updateUser(currentParticipantB)
        }
        binding.duoActivityImageViewRemovePoint2.setOnLongClickListener {
            currentParticipantB.score -= 10
            viewModel.updateUser(currentParticipantB)
            return@setOnLongClickListener true
        }
    }


    private fun setOnClickEditIcon() {
        // Set edit click participant A
        binding.duoActivityImageViewMenu1.setOnClickListener {
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
        binding.duoActivityImageViewMenu2.setOnClickListener {
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
        val toolbarBackImg: ImageView = findViewById(R.id.toolbar_image_view_back)
        val toolbarMenu: ImageView = findViewById(R.id.toolbar_image_view_menu)
        val toolbarDiceImg: ImageView = findViewById(R.id.toolbar_image_view_dice)
        val toolbarAdd: ImageView = findViewById(R.id.toolbar_image_add)

        toolbarAdd.setOnClickListener { viewModel.addUser(User(0,"Guest", 0, viewModel.getRandomColor())) }

        toolbarDiceImg.setOnClickListener {
            viewModel.launchDice()
            DialogDiceResult(viewModel).show(supportFragmentManager, "dialog_dice")
        }

        toolbarBackImg.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        toolbarMenu.setOnClickListener {
            DialogMenu(viewModel).show(supportFragmentManager, "dialog_menu")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("spinner_a_position", spinnerAPos)
        outState.putInt("spinner_b_position", spinnerBPos)
    }
}