package com.example.pointcounter.ui.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pointcounter.databinding.FragmentHistoryBinding
import com.example.pointcounter.viewmodel.SharedViewModel


class HistoryFragment(private val viewModel: SharedViewModel) : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}