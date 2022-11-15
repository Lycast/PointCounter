package com.example.scorecounter.ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.scorecounter.R
import com.example.scorecounter.databinding.AlertDialogMenuBinding
import com.example.scorecounter.model.entity.User
import com.example.scorecounter.viewmodel.SharedViewModel

class DialogMenu : DialogFragment() {

    val viewModel by activityViewModels<SharedViewModel>()

    private lateinit var dialogBinding: AlertDialogMenuBinding
    private var list = listOf<User>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogBinding = AlertDialogMenuBinding.inflate(layoutInflater)
        return activity?.let { activity ->
            val alertDialog = AlertDialog.Builder(activity)
            alertDialog.setView(dialogBinding.root)

            viewModel.users.observe(this) { list = it }

            alertDialog.setPositiveButton(R.string.back_button)  { _, _ -> dismiss() }

            dialogBinding.dialogBtnResetScore.setOnClickListener {
                viewModel.resetAllUsersPoint(list)
            }

            dialogBinding.dialogBtnExit.setOnClickListener { activity.finishAndRemoveTask() }

            alertDialog.create()

        }?: throw IllegalStateException("activity is null")
    }
}