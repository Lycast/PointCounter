package com.example.pointcounter.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.pointcounter.R
import com.example.pointcounter.databinding.ActivityHomeBinding
import com.google.android.material.snackbar.Snackbar

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startCounter()
        setOnClickBottomNavigation()

        setToolbar()
    }

    private fun startCounter() {
        binding.btnStartListPointCounter.setOnClickListener {
            startActivity(Intent(this, CounterListActivity::class.java))
        }
        binding.btnStartSoloCounter.setOnClickListener {
            startActivity(Intent(this, CounterSoloActivity::class.java))
        }
        binding.btnStartDuoCounter.setOnClickListener {
            startActivity(Intent(this, CounterDuoActivity::class.java))
        }
        binding.btnStartCompactListPointCounter.setOnClickListener {
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

        toolbarBackImg.isVisible = false
    }
}