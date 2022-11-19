package com.example.scorecounter.ui.dialog

import android.app.Dialog
import android.os.Bundle
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
            alertDialog.setNegativeButton(R.string.by_default) { _, _ ->
                viewModel.updateListOfStep(listOf(1, 2, 5, 10))
                viewModel.setStep(1)
                dismiss()
            }

            alertDialog.create()

        } ?: throw IllegalStateException("activity is null")
    }

    private fun setView() {
        viewModel.listOfStep.observe(requireActivity()) {
            dialogBinding.apply {
                tfStep1.hint = it[0].toString()
                tfStep2.hint = it[1].toString()
                tfStep3.hint = it[2].toString()
                tfStep4.hint = it[3].toString()
            }
        }
    }

    private fun setNewStep() {
        dialogBinding.apply {
            var step1 = 1; var step2 = 2; var step3 = 5; var step4 = 10

            if (etStep1.text?.isNotEmpty() == true) step1 = etStep1.text.toString().toInt()
            if (etStep2.text?.isNotEmpty() == true) step2 = etStep2.text.toString().toInt()
            if (etStep3.text?.isNotEmpty() == true) step3 = etStep3.text.toString().toInt()
            if (etStep4.text?.isNotEmpty() == true) step4 = etStep4.text.toString().toInt()

            viewModel.updateListOfStep(listOf(step1, step2, step3, step4))
            viewModel.setStep(step1)
        }
    }
}