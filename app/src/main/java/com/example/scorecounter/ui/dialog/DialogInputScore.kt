package com.example.scorecounter.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.text.InputFilter
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.scorecounter.R
import com.example.scorecounter.databinding.AlertDialogInputBinding
import com.example.scorecounter.viewmodel.SharedViewModel

class DialogInputScore() : DialogFragment() {

    private lateinit var dialogBinding: AlertDialogInputBinding

    val viewModel by activityViewModels<SharedViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogBinding = AlertDialogInputBinding.inflate(layoutInflater)
        return activity?.let { it ->
            val alertDialog = AlertDialog.Builder(it)
            alertDialog.setView(dialogBinding.root)

            val user = viewModel.currentUser

            dialogBinding.editText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(8))

            dialogBinding.editText.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                    if (dialogBinding.editText.text.isNotEmpty()) {
                        user.score = dialogBinding.editText.text.toString().toInt()
                        viewModel.updateUser(user)
                        dismiss()
                    } else dismiss()
                    return@OnKeyListener true
                }
                false
            })

            alertDialog.setPositiveButton(R.string.ok) { _, _ ->
                if (dialogBinding.editText.text.isNotEmpty()) {
                    user.score = dialogBinding.editText.text.toString().toInt()
                    viewModel.updateUser(user)
                    dismiss()
                } else dismiss()
            }

            alertDialog.setNegativeButton(R.string.cancel) { _, _ -> dismiss() }

            alertDialog.create()

        } ?: throw IllegalStateException("activity is null")
    }
}