package com.example.scorecounter.ui.navigation

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scorecounter.R
import com.example.scorecounter.databinding.FragmentGameBinding
import com.example.scorecounter.model.entity.User
import com.example.scorecounter.ui.adapter.game.RVGameAdapter
import com.example.scorecounter.ui.dialog.DialogParticipant
import com.example.scorecounter.ui.dialog.DialogPodium
import com.example.scorecounter.utils.OnItemClickListener
import com.example.scorecounter.utils.EnumItem
import com.example.scorecounter.utils.EnumVHSelect
import com.example.scorecounter.viewmodel.SharedViewModel
import com.google.android.material.snackbar.Snackbar
import kotlin.random.Random

class GameFragment : Fragment(), OnItemClickListener {

    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!
    val viewModel by activityViewModels<SharedViewModel>()

    private lateinit var adapterRound : RVGameAdapter
    private lateinit var adapterRank : RVGameAdapter

    private var listParticipant = listOf<User>()
    private var listOfRanking: MutableList<User> = mutableListOf()
    private var listOfRound: MutableList<User> = mutableListOf()
    private var gameIsStarted = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.gameIsStarted.observe(requireActivity()) {
            gameIsStarted = it
            if (!it) {
                binding.gameBtnStart.text = getString(R.string.start)
                binding.gameTvRanking.setText(R.string.participants)
            }
            else {
                binding.gameBtnStart.text = getString(R.string.stop)
                binding.gameTvRanking.setText(R.string.ranking)
            }
        }

        viewModel.listDisplay().observe(requireActivity()) {
            listParticipant = it
            if (!gameIsStarted) {
                listOfRanking.clear()
                listOfRanking.addAll(listParticipant)
                viewModel.updateListOfRanking(listOfRanking)
            }
        }

        initRankingRV()
        initRoundRV()
        setClickListenerView()
    }

    private fun setClickListenerView() {
        // START TOURNAMENT
        binding.gameBtnStart.setOnClickListener {
            if (gameIsStarted) {
                val alertDialog = AlertDialog.Builder(requireActivity())
                alertDialog.setMessage(getString(R.string.cancel_tournament))
                alertDialog.setPositiveButton(R.string.ok) { _, _ ->
                    viewModel.updateGameIsStarted(false)
                    listOfRound.clear()
                    viewModel.updateListOfRound(listOfRound)
                    listOfRanking.clear()
                    listOfRanking.addAll(listParticipant)
                    viewModel.updateListOfRanking(listOfRanking)
                    alertDialog.create().dismiss()
                }
                alertDialog.setNegativeButton(R.string.cancel) { _, _ ->
                    alertDialog.create().dismiss()
                }
                alertDialog.create().show()
            }

            if (!gameIsStarted) {
                if (listParticipant.size >= 3 ) {
                    viewModel.updateGameIsStarted(true)
                    listOfRanking.clear()
                    listOfRanking.addAll(listParticipant)
                    for (i in 0 until listOfRanking.size) {
                        listOfRanking[i].score = 0
                    }
                    viewModel.updateListOfRanking(listOfRanking)
                    listOfRound.clear()
                    generateTournament(listParticipant)
                } else Snackbar.make(binding.root, getString(R.string.alert_game) , Snackbar.LENGTH_SHORT).show()
            }
        }

        // HELPER TEXT
        binding.gameHelpStart.setOnClickListener {
            displayAlertDialogInfo(R.string.help_start)
        }
        binding.tvRoundRemaining.setOnClickListener {
            displayAlertDialogInfo(R.string.round_remaining)
        }
    }

    private fun initRoundRV() {
        viewModel.listOfRound.observe(requireActivity()) {
            // RV Round
            adapterRound = RVGameAdapter(it, this, EnumVHSelect.ROUND, null)
            binding.recyclerViewRound.adapter = adapterRound
            binding.recyclerViewRound.layoutManager = GridLayoutManager( requireActivity(), 2)
            // Display match remaining
            binding.tvRoundRemaining.text = (listOfRound.size / 2).toString()
        }
    }

    private fun initRankingRV() {
        viewModel.listOfRanking.observe(requireActivity()) {
            // RV Rank
            adapterRank = RVGameAdapter(it.sortedByDescending { user -> user.score }, this, EnumVHSelect.RANKING, gameIsStarted)

            binding.recyclerViewRanking.layoutManager = LinearLayoutManager( requireActivity())
            binding.recyclerViewRanking.adapter = adapterRank
        }
    }

    private fun setWinRound(user: User, pos: Int) {
        val i = listOfRanking.indexOf(user)
        user.score ++
        listOfRanking[i] = user
        viewModel.updateListOfRanking(listOfRanking)

        if ((pos+1) % 2 == 1) {
            clearRound(pos, pos)
        } else clearRound(pos, pos - 1)
        viewModel.updateListOfRound(listOfRound)

        // display dialog podium, game end
        if (listOfRound.isEmpty()) {
            DialogPodium().show(parentFragmentManager, "dialog_podium")
            viewModel.updateGameIsStarted(false)
        }
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

        val indexListA = mutableListOf<User>()
        indexListA.addAll(listA)
        val indexListB = mutableListOf<User>()
        indexListB.addAll(listB)

        for (i in listA.indices) {
            val randomIndex = Random.nextInt(indexListA.size)
            listOfRound.add(indexListA[randomIndex])
            listOfRound.add(indexListB[randomIndex])
            indexListA.removeAt(randomIndex)
            indexListB.removeAt(randomIndex)
        }
        viewModel.updateListOfRound(listOfRound)
    }

    private fun clearRound(posA: Int, posB: Int){
        listOfRound.removeAt(posA)
        listOfRound.removeAt(posB)
    }

    private fun displayAlertDialogInfo(textId: Int) {
        val alertDialog = AlertDialog.Builder(requireActivity())
        alertDialog.setMessage(textId)
        alertDialog.setPositiveButton(R.string.ok) {_,_ ->
            alertDialog.create().dismiss()
        }
        alertDialog.create().show()
    }

    override fun setOnItemClickListener(user: User, enum: EnumItem, pos: Int?) {
        viewModel.currentUser = user
        when (enum) {
            EnumItem.ROUND_WIN -> setWinRound(user, pos!!)
            EnumItem.EDIT -> DialogParticipant().show(parentFragmentManager, "dialog_user")
            else -> {}
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}