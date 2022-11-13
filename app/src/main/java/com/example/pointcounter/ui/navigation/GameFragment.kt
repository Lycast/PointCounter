package com.example.pointcounter.ui.navigation

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pointcounter.R
import com.example.pointcounter.databinding.FragmentGameBinding
import com.example.pointcounter.model.entity.User
import com.example.pointcounter.ui.adapter.ParticipantAdapter
import com.example.pointcounter.ui.dialog.DialogDiceResult
import com.example.pointcounter.ui.dialog.DialogDiceSetup
import com.example.pointcounter.ui.dialog.DialogParticipant
import com.example.pointcounter.utils.OnItemClickListener
import com.example.pointcounter.utils.EnumItem
import com.example.pointcounter.utils.EnumVHSelect
import com.example.pointcounter.viewmodel.SharedViewModel

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
            gameIsStarted = true
            initParticipantsRV()
            listOfTournament.clear()
            viewModel.resetAllUsersPoint(listParticipant)
            generateTournament(listParticipant)
            binding.gameTvRanking.setText(R.string.ranking)
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
            viewModel.launchNormalDice()
            DialogDiceResult(viewModel,false).show(parentFragmentManager, "dialog_dice")
        }
        binding.gameImageEditDice.setOnClickListener {
            DialogDiceSetup(viewModel).show(parentFragmentManager, "dialog_dice_setup")
        }

        // HELPER TEXT
        binding.gameHelpResult.setOnClickListener {
            displayAlertDialogInfo(R.string.help_ranking)
        }
        binding.gameHelpRound.setOnClickListener {
            displayAlertDialogInfo(R.string.help_round)
        }
        binding.gameHelpStart.setOnClickListener {
            displayAlertDialogInfo(R.string.help_start)
        }
        binding.gameRemainingText.setOnClickListener {
            displayAlertDialogInfo(R.string.round_remaining)
        }
    }

    private fun initRoundRV() {
        viewModel.listOfTournament.observe(requireActivity()) {
            if (it.isEmpty() && gameIsStarted) {
                //todo add alert with winners
                gameIsStarted = false
                initParticipantsRV()
            }
            // RV Round
            adapterRound = ParticipantAdapter(it, this, EnumVHSelect.ROUND, null)
            binding.recyclerViewRound.adapter = adapterRound
            binding.recyclerViewRound.layoutManager = GridLayoutManager( requireActivity(), 2)
            // Display match remaining
            binding.gameRemainingText.text = (listOfTournament.size / 2).toString()
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