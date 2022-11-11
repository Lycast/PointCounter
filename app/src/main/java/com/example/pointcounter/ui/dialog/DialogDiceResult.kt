package com.example.pointcounter.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.pointcounter.databinding.AlertDialogDiceResultBinding
import com.example.pointcounter.viewmodel.SharedViewModel

class DialogDiceResult(private val viewModel: SharedViewModel) : DialogFragment() {

    private lateinit var dialogBinding: AlertDialogDiceResultBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogBinding = AlertDialogDiceResultBinding.inflate(LayoutInflater.from(context))
        return activity?.let { it ->
            val alertDialog = AlertDialog.Builder(it)
            alertDialog.setView(dialogBinding.root)

            var color = viewModel.getRandomColor()

            // Populate alert dialog
            dialogBinding.apply {
                viewModel.diceResult.observe(requireActivity()) { result ->
                    dialogTextResult.text = result
                    dialogTextResult.setTextColor(color)
                    dialogImgDice.setColorFilter(color)
                    textRoll.setTextColor(color)
                }
            }

            // On click button roll dice
            dialogBinding.dialogImgDice.setOnClickListener {
                color = viewModel.getRandomColor()
                viewModel.launchDice()
            }

            // On click button Ok
            dialogBinding.layoutDialogDiceResult.setOnClickListener {
                dismiss()
            }
            alertDialog.create()

        }?: throw IllegalStateException("activity is null")
    }
}