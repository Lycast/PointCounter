package com.example.pointcounter.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pointcounter.R
import com.example.pointcounter.databinding.ActivityCounterBinding

class CounterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCounterBinding
    private var numberOfGuest = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCounterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.hide()

        numberOfGuest = intent.extras?.get("nbOfGuest") as Int

        when (numberOfGuest) {
            1 -> setContentView(R.layout.fragment_1_guest)
            2 -> setContentView(R.layout.fragment_2_guest)
        }
    }
}