package com.example.pointcounter.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
    }

    private fun startCounter() {
        binding.btnStartListPointCounter.setOnClickListener {
            startActivity(Intent(this, PointCounterListActivity::class.java))
        }
        binding.btnStartSoloCounter.setOnClickListener {
            startActivity(Intent(this, SoloCounterActivity::class.java))
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
}