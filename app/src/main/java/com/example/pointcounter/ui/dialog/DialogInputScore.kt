package com.example.pointcounter.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.pointcounter.R
import com.example.pointcounter.databinding.AlertDialogInputScoreBinding
import com.example.pointcounter.model.entity.User
import com.example.pointcounter.utils.EnumDialogEditText
import com.example.pointcounter.viewmodel.SharedViewModel
import com.google.android.material.snackbar.Snackbar

class DialogInputScore (private var user: User, private val viewModel: SharedViewModel,private val enum: EnumDialogEditText) : DialogFragment() {

    private lateinit var dialogBinding: AlertDialogInputScoreBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogBinding = AlertDialogInputScoreBinding.inflate(LayoutInflater.from(context))
        return activity?.let { it ->
            val alertDialog = AlertDialog.Builder(it)
            alertDialog.setView(dialogBinding.root)

            //dialogBinding.editText.requestFocus()

            alertDialog.setPositiveButton(R.string.ok) {_,_ ->
                when(enum) {
                    EnumDialogEditText.SCORE_INPUT -> {
                        if (dialogBinding.editText.text.isNotEmpty()){
                            user.score = dialogBinding.editText.text.toString().toInt()
                            viewModel.updateUser(user)
                            dismiss()
                        } else Snackbar.make(dialogBinding.root, "Enter something", Snackbar.LENGTH_SHORT).show()
                    }
                    EnumDialogEditText.STEP_INPUT -> {
                        if (dialogBinding.editText.text.isNotEmpty()){
                            viewModel.setStep(dialogBinding.editText.text.toString().toInt())
                            dismiss()
                        } else Snackbar.make(dialogBinding.root, "Enter something", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }

            alertDialog.setNegativeButton(R.string.cancel) {_,_ -> dismiss() }

            alertDialog.create()

        }?: throw IllegalStateException("activity is null")
    }
}