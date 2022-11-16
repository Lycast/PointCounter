package com.example.scorecounter.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.scorecounter.R
import com.example.scorecounter.databinding.AlertDialogInputStepBinding
import com.example.scorecounter.viewmodel.SharedViewModel

class DialogInputStep : DialogFragment() {

    private lateinit var dialogBinding: AlertDialogInputStepBinding

    val viewModel by activityViewModels<SharedViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogBinding = AlertDialogInputStepBinding.inflate(layoutInflater)
        return activity?.let { it ->
            val alertDialog = AlertDialog.Builder(it)
            alertDialog.setView(dialogBinding.root)

            setView()

            alertDialog.setPositiveButton(R.string.ok) { _, _ -> setNewStep() }
            alertDialog.setNegativeButton(R.string.cancel) { _, _ -> dismiss() }

            alertDialog.create()

        } ?: throw IllegalStateException("activity is null")
    }

    private fun setView() {
        viewModel.listOfStep.observe(requireActivity()) {
            dialogBinding.apply {
                tfStep1.hint = it[1].toString()
                tfStep2.hint = it[2].toString()
                tfStep3.hint = it[3].toString()
                Log.e("MY-TAG", "vm observer list = $it")
            }
        }
    }

    private fun setNewStep() {
        dialogBinding.apply {
            val step0 = 1; var step1 = 2; var step2 = 5; var step3 = 10

            if (etStep1.text?.isNotEmpty() == true) step1 = etStep1.text.toString().toInt()
            if (etStep2.text?.isNotEmpty() == true) step2 = etStep2.text.toString().toInt()
            if (etStep3.text?.isNotEmpty() == true) step3 = etStep3.text.toString().toInt()

            Log.e("MY-TAG", "step 1 setNewStep = $step1")

            viewModel.updateListOfStep(listOf(step0, step1, step2, step3))
        }
    }
}