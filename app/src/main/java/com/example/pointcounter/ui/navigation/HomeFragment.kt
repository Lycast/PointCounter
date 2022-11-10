package com.example.pointcounter.ui.navigation

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pointcounter.databinding.FragmentHomeBinding
import com.example.pointcounter.ui.CounterCompactListActivity
import com.example.pointcounter.ui.CounterDuoActivity
import com.example.pointcounter.ui.CounterListActivity
import com.example.pointcounter.ui.CounterSoloActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startCounter()
    }

    private fun startCounter() {
        binding.imageViewListCard.setOnClickListener {
            startActivity(Intent(context, CounterListActivity::class.java))
        }
        binding.imageViewSoloCard.setOnClickListener {
            startActivity(Intent(context, CounterSoloActivity::class.java))
        }
        binding.imageViewDuoCard.setOnClickListener {
            startActivity(Intent(context, CounterDuoActivity::class.java))
        }
        binding.imageViewCompactListCard.setOnClickListener {
            startActivity(Intent(context, CounterCompactListActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}