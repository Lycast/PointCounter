package com.example.pointcounter.ui.navigation

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pointcounter.R
import com.example.pointcounter.databinding.FragmentHomeBinding
import com.example.pointcounter.model.entity.User
import com.example.pointcounter.ui.*
import com.example.pointcounter.ui.adapter.ParticipantAdapter
import com.example.pointcounter.ui.dialog.DialogDiceResult
import com.example.pointcounter.ui.dialog.DialogParticipant
import com.example.pointcounter.utils.OnItemClickListener
import com.example.pointcounter.utils.UserEnum
import com.example.pointcounter.utils.ViewHolderEnum
import com.example.pointcounter.viewmodel.SharedViewModel
import com.google.android.material.snackbar.Snackbar

class HomeFragment(private val viewModel: SharedViewModel) : Fragment(), OnItemClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter : ParticipantAdapter
    private var listSize = 0


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

        // On click start counter (check list size before launch counter)
        binding.homeButtonSoloCounter.setOnClickListener {
            if (listSize > 0) {
                startActivity(Intent(context, CounterSoloActivity::class.java))
                activity?.finish()
            }
            else snackBar(getString(R.string.alert_add_participant))
        }
        binding.homeButtonListCounter.setOnClickListener {
            if (listSize > 0) {
                startActivity(Intent(context, CounterListActivity::class.java))
                activity?.finish()
            } else snackBar(getString(R.string.alert_add_participant))
        }
        binding.homeButtonCompactListCounter.setOnClickListener {
            if (listSize > 0) {
                startActivity(Intent(context, CounterCompactListActivity::class.java))
                activity?.finish()
            } else snackBar(getString(R.string.alert_add_participant))
        }
        binding.homeButtonDuoCounter.setOnClickListener {
            if (listSize > 1) {
                startActivity(Intent(context, CounterDuoActivity::class.java))
                activity?.finish()
            } else snackBar(getString(R.string.alert_add_participant))
        }

        // On click image add participant
        binding.homeImgAdd.setOnClickListener {
            viewModel.addUser(User(0,"Guest",0,viewModel.getRandomColor()))
        }
        binding.homeImgAddEmpty.setOnClickListener {
            viewModel.addUser(User(0,"Guest",0,viewModel.getRandomColor()))
        }

        // On click delete all participants
        binding.homeImgDelete.setOnClickListener {
            val alertDialog = AlertDialog.Builder(context)
            alertDialog.setTitle(R.string.delete_all_participant_alert)
            alertDialog.setPositiveButton(R.string.yes) { _, _ ->
                viewModel.deleteAllUsers()
                snackBar(getString(R.string.info_all_participant_deleted))
            }
            alertDialog.setNegativeButton(R.string.no) {_,_ -> alertDialog.create().dismiss()}
            alertDialog.create().show()
        }

        // On click image dice
        binding.imgDice.setOnClickListener {
            viewModel.launchDice()
            DialogDiceResult(viewModel).show(parentFragmentManager, "dialog_dice")
        }

        // On click edit dice
        binding.homeImageEditDice.setOnClickListener {
            snackBar("Not yet implemented")
        }
    }

    private fun displayList() {
        viewModel.users.observe(requireActivity()) {
            adapter = ParticipantAdapter(it, this, ViewHolderEnum.HOME)
            binding.recyclerViewGuests.adapter = adapter
            binding.recyclerViewGuests.layoutManager = LinearLayoutManager(context)
            listSize = it.size

            if (it.isEmpty()) {
                binding.homeImgAddEmpty.visibility = View.VISIBLE
                binding.homeTextAddEmpty.visibility = View.VISIBLE
            } else {
                binding.homeImgAddEmpty.visibility = View.GONE
                binding.homeTextAddEmpty.visibility = View.GONE
            }
        }
    }

    override fun setOnItemClickListener(user: User, enum: UserEnum) {
        when (enum) {
            UserEnum.DELETE -> viewModel.deleteUser(user)
            UserEnum.EDIT -> DialogParticipant(user, viewModel).show(parentFragmentManager, "dialog_user")
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