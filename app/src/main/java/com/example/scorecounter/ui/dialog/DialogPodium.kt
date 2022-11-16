package com.example.scorecounter.ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.scorecounter.R
import com.example.scorecounter.databinding.AlertDialogWinnersBinding
import com.example.scorecounter.model.entity.User
import com.example.scorecounter.viewmodel.SharedViewModel

class DialogPodium() : DialogFragment() {

    private lateinit var dialogBinding: AlertDialogWinnersBinding
    val viewModel by activityViewModels<SharedViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogBinding = AlertDialogWinnersBinding.inflate(layoutInflater)
        return activity?.let { it ->
            val alertDialog = AlertDialog.Builder(it)
            alertDialog.setView(dialogBinding.root)

            var list: List<User> = listOf()
            viewModel.listOfRanking.observe(requireActivity()) { list = it.sortedByDescending { user -> user.score } }

            // set first
            val name1 = list[0].name
            val score1 = list[0].score
            val text1 = "$name1\nscore: $score1"
            dialogBinding.imgFirst.setColorFilter(ContextCompat.getColor(requireActivity(), R.color.yellow))
            dialogBinding.nameFirst.text = text1

            // set second
            val name2 = list[1].name
            val score2 = list[1].score
            val text2 = "$name2\nscore: $score2"
            dialogBinding.imgSecond.setColorFilter(ContextCompat.getColor(requireActivity(), R.color.light_gray))
            dialogBinding.nameSecond.text = text2

            // set third
            val name3 = list[2].name
            val score3 = list[2].score
            val text3 = "$name3\nscore: $score3"
            dialogBinding.imgThird.setColorFilter(ContextCompat.getColor(requireActivity(), R.color.orange))
            dialogBinding.nameThird.text = text3

            alertDialog.setPositiveButton(R.string.back_button)  { _, _ ->
                alertDialog.create().dismiss()
            }

            alertDialog.create()
        }?: throw IllegalStateException("activity is null")
    }
}