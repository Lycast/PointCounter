package com.example.pointcounter.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.fragment.app.DialogFragment
import com.example.pointcounter.R
import com.example.pointcounter.databinding.AlertDialogParticipantBinding
import com.example.pointcounter.model.entity.User
import com.example.pointcounter.viewmodel.SharedViewModel

class DialogParticipant(private val user: User?, private val viewModel: SharedViewModel) : DialogFragment() {

    private lateinit var dialogBinding: AlertDialogParticipantBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogBinding = AlertDialogParticipantBinding.inflate(LayoutInflater.from(context))
        return activity?.let {
            val alertDialog = AlertDialog.Builder(it)
            alertDialog.setView(dialogBinding.root)

            var color = user?.color ?: viewModel.getRandomColor()

            viewModel.setColor(color.red, color.green, color.blue)
            viewModel.color.observe(this) { newColor ->
                color = newColor
                dialogBinding.cardExampleColor.setCardBackgroundColor(color)
                dialogBinding.btnGenerateColor.setTextColor(color)
            }

            // Populate alert dialog
            dialogBinding.apply {
                if ( user == null ) {
                    cardExampleColor.setCardBackgroundColor(color)
                    btnGenerateColor.setTextColor(color)
                } else {
                    alertDialogTitle.setText(R.string.edit_participant)
                    editTextEnterName.setText(user.name)
                    btnGenerateColor.setTextColor(user.color)
                    cardExampleColor.setCardBackgroundColor(user.color)
                }
            }

            dialogBinding.cardExampleColor.setOnClickListener {
                DialogPickerColor(viewModel).show(childFragmentManager, "dialog_color")
            }

            // On click button generate color
            dialogBinding.btnGenerateColor.setOnClickListener {
                color = viewModel.getRandomColor()
                dialogBinding.cardExampleColor.setCardBackgroundColor(color)
                dialogBinding.btnGenerateColor.setTextColor(color)
            }

            // On click button Ok
            dialogBinding.alertDialogButtonOk.setOnClickListener {

                // Update guest
                if (user != null && !TextUtils.isEmpty(dialogBinding.editTextEnterName.text)) {
                    viewModel.updateUser(User(user.id, dialogBinding.editTextEnterName.text.toString(), user.score, color))
                    dismiss()
                } else if (user != null && TextUtils.isEmpty(dialogBinding.editTextEnterName.text)) {
                    viewModel.updateUser(User(user.id, user.name, user.score, color))
                    dismiss()
                }

                //  Add new guest
                if (user == null && !TextUtils.isEmpty(dialogBinding.editTextEnterName.text)) {
                    val newUser = User(0, dialogBinding.editTextEnterName.text.toString(), 0, color)
                    viewModel.addUser(newUser)
                    dismiss()
                } else if (user == null && TextUtils.isEmpty(dialogBinding.editTextEnterName.text)) {
                    val newUser = User(0, "Guest", 0, color)
                    viewModel.addUser(newUser)
                    dismiss()
                }
            }
            alertDialog.create()

        }?: throw IllegalStateException("activity is null")
    }
}