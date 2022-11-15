package com.example.scorecounter.ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.scorecounter.R
import com.example.scorecounter.databinding.AlertDialogWinnersBinding
import com.example.scorecounter.model.entity.User

class DialogPodium(private val list: List<User>) : DialogFragment() {

    private lateinit var dialogBinding: AlertDialogWinnersBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogBinding = AlertDialogWinnersBinding.inflate(layoutInflater)
        return activity?.let { it ->
            val alertDialog = AlertDialog.Builder(it)
            alertDialog.setView(dialogBinding.root)

            // set first
            dialogBinding.imgFirst.setColorFilter(ContextCompat.getColor(requireActivity(), R.color.yellow))
            dialogBinding.nameFirst.text = list[0].name

            // set second
            dialogBinding.imgSecond.setColorFilter(ContextCompat.getColor(requireActivity(), R.color.light_gray))
            dialogBinding.nameSecond.text = list[1].name

            // set third
            dialogBinding.imgThird.setColorFilter(ContextCompat.getColor(requireActivity(), R.color.orange))
            dialogBinding.nameThird.text = list[2].name

            alertDialog.setPositiveButton(R.string.back_button)  { _, _ -> dismiss() }

            alertDialog.create()
        }?: throw IllegalStateException("activity is null")
    }
}