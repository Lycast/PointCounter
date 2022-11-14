package com.example.scorecounter.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.scorecounter.R
import com.example.scorecounter.databinding.AlertDialogInputBinding
import com.example.scorecounter.model.entity.User
import com.example.scorecounter.utils.EnumDialogInput
import com.example.scorecounter.viewmodel.SharedViewModel

class DialogInput (private var user: User?, private val viewModel: SharedViewModel, private val enum: EnumDialogInput) : DialogFragment() {

    private lateinit var dialogBinding: AlertDialogInputBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogBinding = AlertDialogInputBinding.inflate(LayoutInflater.from(context))
        return activity?.let { it ->
            val alertDialog = AlertDialog.Builder(it)
            alertDialog.setView(dialogBinding.root)


            dialogBinding.editText.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                    when(enum) {

                        EnumDialogInput.SCORE_INPUT -> {
                            if (dialogBinding.editText.text.isNotEmpty()) {
                                user?.let { userIt ->
                                    userIt.score = dialogBinding.editText.text.toString().toInt()
                                    viewModel.updateUser(userIt) }
                                dismiss()
                            } else dismiss()
                        }

                        EnumDialogInput.STEP_INPUT -> {
                                if (dialogBinding.editText.text.isNotEmpty()) {
                                    viewModel.setStep(dialogBinding.editText.text.toString().toInt())
                                    dismiss()
                                } else dismiss()
                        }
                    }
                    return@OnKeyListener true
                }
                false
            })

            when(enum) {
                EnumDialogInput.SCORE_INPUT -> {
                    alertDialog.setPositiveButton(R.string.ok) { _, _ ->
                        if (dialogBinding.editText.text.isNotEmpty()) {
                            user?.let { userIt ->
                                userIt.score = dialogBinding.editText.text.toString().toInt()
                                viewModel.updateUser(userIt) }
                            dismiss()
                        } else dismiss()
                    }
                }
                EnumDialogInput.STEP_INPUT -> {
                    dialogBinding.editText.hint = getString(R.string.step)
                    alertDialog.setPositiveButton(R.string.ok) { _, _ ->
                        if (dialogBinding.editText.text.isNotEmpty()) {
                            viewModel.setStep(dialogBinding.editText.text.toString().toInt())
                            dismiss()
                        } else dismiss()
                    }
                }
            }

            alertDialog.setNegativeButton(R.string.cancel) {_,_ -> dismiss() }

            alertDialog.create()

        }?: throw IllegalStateException("activity is null")
    }
}