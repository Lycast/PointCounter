package com.example.scorecounter.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.scorecounter.R
import com.example.scorecounter.databinding.AlertDialogParticipantBinding
import com.example.scorecounter.model.entity.User
import com.example.scorecounter.viewmodel.SharedViewModel

class DialogParticipant : DialogFragment() {

    private lateinit var dialogBinding: AlertDialogParticipantBinding
    val viewModel by activityViewModels<SharedViewModel>()



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogBinding = AlertDialogParticipantBinding.inflate(layoutInflater)
        return activity?.let {
            val alertDialog = AlertDialog.Builder(it)
            alertDialog.setView(dialogBinding.root)

            val user = viewModel.currentUser

            var color = user.color

            viewModel.setColor(color.red, color.green, color.blue)
            viewModel.color.observe(this) { newColor ->
                color = newColor
                dialogBinding.cardExampleColor.setCardBackgroundColor(color)
                dialogBinding.btnGenerateColor.setTextColor(color)
            }

            // Populate alert dialog
            dialogBinding.apply {
                editTextEnterName.hint = user.name
                btnGenerateColor.setTextColor(user.color)
                cardExampleColor.setCardBackgroundColor(user.color)
            }

            dialogBinding.cardExampleColor.setOnClickListener {
                DialogPickerColor().show(childFragmentManager, "dialog_color")
            }

            // On click button generate color
            dialogBinding.btnGenerateColor.setOnClickListener {
                color = viewModel.getRndColor()
                dialogBinding.cardExampleColor.setCardBackgroundColor(color)
                dialogBinding.btnGenerateColor.setTextColor(color)
            }

            alertDialog.setPositiveButton(R.string.ok) {_,_ ->
                // Update guest
                if (!TextUtils.isEmpty(dialogBinding.editTextEnterName.text)) {
                    viewModel.updateUser(User(user.id, dialogBinding.editTextEnterName.text.toString(), user.score, color))
                    dismiss()
                } else if (TextUtils.isEmpty(dialogBinding.editTextEnterName.text)) {
                    viewModel.updateUser(User(user.id, user.name, user.score, color))
                    dismiss()
                }
            }

            alertDialog.setNegativeButton(R.string.cancel) {_,_ -> dismiss() }

            alertDialog.create()

        }?: throw IllegalStateException("activity is null")
    }
}