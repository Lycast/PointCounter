package com.example.scorecounter.ui.navigation

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.scorecounter.R
import com.example.scorecounter.databinding.FragmentGameBinding
import com.example.scorecounter.model.entity.User
import com.example.scorecounter.ui.adapter.ParticipantAdapter
import com.example.scorecounter.ui.dialog.DialogDiceResult
import com.example.scorecounter.ui.dialog.DialogDiceSetup
import com.example.scorecounter.ui.dialog.DialogParticipant
import com.example.scorecounter.ui.dialog.DialogPodium
import com.example.scorecounter.utils.OnItemClickListener
import com.example.scorecounter.utils.EnumItem
import com.example.scorecounter.utils.EnumVHSelect
import com.example.scorecounter.viewmodel.SharedViewModel
import com.google.android.material.snackbar.Snackbar

class GameFragment(private val viewModel: SharedViewModel) : Fragment(), OnItemClickListener {

    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapterRound : ParticipantAdapter
    private lateinit var adapterRank : ParticipantAdapter

    private var listParticipant = listOf<User>()
    private var listOfTournament: MutableList<User> = mutableListOf()

    private var gameIsStarted = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initParticipantsRV()
        initRoundRV()
        setClickListenerView()
    }

    private fun setClickListenerView() {

        // START TOURNAMENT
        binding.gameBtnStart.setOnClickListener {
            if (listParticipant.size >= 3) {
                gameIsStarted = true
                initParticipantsRV()
                listOfTournament.clear()
                viewModel.resetAllUsersPoint(listParticipant)
                generateTournament(listParticipant)
                binding.gameTvRanking.setText(R.string.ranking)
            } else Snackbar.make(binding.root, getString(R.string.alert_game) , Snackbar.LENGTH_SHORT).show()
        }

        // CANCEL TOURNAMENT
        binding.gameDelete.setOnClickListener {
            val alertDialog = AlertDialog.Builder(requireActivity())
            alertDialog.setMessage(getString(R.string.cancel_tournament))
            alertDialog.setPositiveButton(R.string.ok) {_,_ ->
                listOfTournament.clear()
                viewModel.updateListOfTournament(listOfTournament)
                viewModel.resetAllUsersPoint(listParticipant)
                alertDialog.create().dismiss()
            }
            alertDialog.setNegativeButton(R.string.cancel) {_,_ -> alertDialog.create().dismiss()}
            alertDialog.create().show()
        }

        // DICE
        binding.imgDice.setOnClickListener {
            DialogDiceResult(viewModel).show(parentFragmentManager, "dialog_dice")
        }
        binding.gameEditDiceImg.setOnClickListener {
            DialogDiceSetup(viewModel).show(parentFragmentManager, "dialog_dice_setup")
        }

        // HELPER TEXT
        binding.ivHelpRanking.setOnClickListener {
            displayAlertDialogInfo(R.string.help_ranking)
        }
        binding.ivHelpRound.setOnClickListener {
            displayAlertDialogInfo(R.string.help_round)
        }
        binding.gameHelpStart.setOnClickListener {
            displayAlertDialogInfo(R.string.help_start)
        }
        binding.tvRoundRemaining.setOnClickListener {
            displayAlertDialogInfo(R.string.round_remaining)
        }
    }

    private fun initRoundRV() {
        viewModel.listOfTournament.observe(requireActivity()) {
            if (it.isEmpty() && gameIsStarted) {
                gameIsStarted = false

                // display dialog podium
                DialogPodium(listParticipant).show(parentFragmentManager, "dialog_podium")

                // int rv for update game end
                initParticipantsRV()
            }

            // RV Round
            adapterRound = ParticipantAdapter(it, this, EnumVHSelect.ROUND, null)
            binding.recyclerViewRound.adapter = adapterRound
            binding.recyclerViewRound.layoutManager = GridLayoutManager( requireActivity(), 2)
            // Display match remaining
            binding.tvRoundRemaining.text = (listOfTournament.size / 2).toString()
        }
    }

    private fun initParticipantsRV() {
        viewModel.users.observe(requireActivity()) {
            // RV Rank
            adapterRank = ParticipantAdapter(it.sortedByDescending { user -> user.score }, this, EnumVHSelect.RANKING, gameIsStarted)
            binding.recyclerViewRanking.adapter = adapterRank
            listParticipant = it
        }
    }

    override fun setOnItemClickListener(user: User, enum: EnumItem, pos: Int) {
        when (enum) {
            EnumItem.ROUND_WIN -> setWinRoundListener(user, pos)
            EnumItem.EDIT -> DialogParticipant(user, viewModel).show(parentFragmentManager, "dialog_user")
            else -> {}
        }
    }

    private fun setWinRoundListener(user: User, pos: Int) {
        user.score += 1
        viewModel.updateUser(user)
        if ((pos+1) % 2 == 1) {
            clearRound(pos, pos)
        } else clearRound(pos, pos - 1)
        viewModel.updateListOfTournament(listOfTournament)
    }

    private fun generateTournament(list: List<User>) {
        val listA: MutableList<User> = mutableListOf()
        val listB: MutableList<User> = mutableListOf()
        for (a in list.indices) {
            for (b in a until list.size) {
                if (a != b) {
                    listA.add(list[a])
                    listB.add(list[b])
                }
            }
        }
        // is here for randomize list
        for (i in listA.indices) {
            listOfTournament.add(listA[i])
            listOfTournament.add(listB[i])
        }
        viewModel.updateListOfTournament(listOfTournament)
    }

    private fun clearRound(posA: Int, posB: Int){
        listOfTournament.removeAt(posA)
        listOfTournament.removeAt(posB)
    }

    private fun displayAlertDialogInfo(textId: Int) {
        val alertDialog = AlertDialog.Builder(requireActivity())
        alertDialog.setMessage(textId)
        alertDialog.setPositiveButton(R.string.ok) {_,_ ->
            alertDialog.create().dismiss()
        }
        alertDialog.create().show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}