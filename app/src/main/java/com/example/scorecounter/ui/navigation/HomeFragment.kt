package com.example.scorecounter.ui.navigation

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scorecounter.R
import com.example.scorecounter.databinding.FragmentHomeBinding
import com.example.scorecounter.model.entity.User
import com.example.scorecounter.ui.adapter.nav.RVNavAdapter
import com.example.scorecounter.ui.dialog.DialogParticipant
import com.example.scorecounter.utils.OnItemClickListener
import com.example.scorecounter.utils.EnumItem
import com.example.scorecounter.utils.EnumVHSelect
import com.example.scorecounter.viewmodel.SharedViewModel
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment(), OnItemClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter : RVNavAdapter
    private var listSize = 0
    val viewModel by activityViewModels<SharedViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        displayList()
        setClickListenerView()
    }

    private fun setClickListenerView() {
        // ADD PARTICIPANT
        binding.ivAdd.setOnClickListener {
            viewModel.addUser(User(0,viewModel.getRndName(),0,viewModel.getRndColor()))
        }
        binding.homeImgAddEmpty.setOnClickListener {
            viewModel.addUser(User(0,viewModel.getRndName(),0,viewModel.getRndColor()))
        }

        // DELETE ALL PARTICIPANT
        binding.ivDeleteAll.setOnClickListener {
            val alertDialog = AlertDialog.Builder(context)
            alertDialog.setTitle(R.string.delete_all_participant_alert)
            alertDialog.setPositiveButton(R.string.yes) { _, _ ->
                viewModel.deleteAllUsers()
                snackBar(getString(R.string.info_all_participant_deleted))
            }
            alertDialog.setNegativeButton(R.string.no) {_,_ -> alertDialog.create().dismiss()}
            alertDialog.create().show()
        }
    }

    private fun displayList() {
        viewModel.users.observe(requireActivity()) {
            adapter = RVNavAdapter(it, this, EnumVHSelect.HOME, null)
            binding.recyclerViewGuests.adapter = adapter
            binding.recyclerViewGuests.layoutManager = LinearLayoutManager(context)
            listSize = it.size

            if (it.isEmpty()) {
                binding.homeImgAddEmpty.visibility = View.VISIBLE
                binding.tvAddEmpty.visibility = View.VISIBLE
            } else {
                binding.homeImgAddEmpty.visibility = View.GONE
                binding.tvAddEmpty.visibility = View.GONE
            }
        }
    }

    override fun setOnItemClickListener(user: User, enum: EnumItem, pos: Int) {
        viewModel.currentUser = user
        when (enum) {
            EnumItem.DELETE -> {
                val alertDialog = AlertDialog.Builder(context)
                alertDialog.setTitle(R.string.delete_counter)
                alertDialog.setPositiveButton(R.string.yes) { _, _ ->
                    viewModel.deleteUser(user)
                }
            }
            EnumItem.EDIT -> DialogParticipant().show(parentFragmentManager, "dialog_user")
            else -> {}
        }
    }

    private fun snackBar(text: String) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}