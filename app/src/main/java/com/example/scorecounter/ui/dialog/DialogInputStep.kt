package com.example.scorecounter.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.scorecounter.R
import com.example.scorecounter.databinding.AlertDialogInputBinding
import com.example.scorecounter.viewmodel.SharedViewModel

class DialogInputStep() : DialogFragment() {

    private lateinit var dialogBinding: AlertDialogInputBinding

    val viewModel by activityViewModels<SharedViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogBinding = AlertDialogInputBinding.inflate(layoutInflater)
        return activity?.let { it ->
            val alertDialog = AlertDialog.Builder(it)
            alertDialog.setView(dialogBinding.root)

            dialogBinding.editText.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                    if (dialogBinding.editText.text.isNotEmpty()) {
                        viewModel.setStep(dialogBinding.editText.text.toString().toInt())
                        dismiss()
                    } else dismiss()
                    return@OnKeyListener true
                }
                false
            })

            dialogBinding.editText.hint = getString(R.string.step)
            alertDialog.setPositiveButton(R.string.ok) { _, _ ->
                if (dialogBinding.editText.text.isNotEmpty()) {
                    viewModel.setStep(dialogBinding.editText.text.toString().toInt())
                    dismiss()
                } else dismiss()
            }

            alertDialog.setNegativeButton(R.string.cancel) { _, _ -> dismiss() }

            alertDialog.create()

        } ?: throw IllegalStateException("activity is null")
    }
}