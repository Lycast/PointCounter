package com.example.pointcounter.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.pointcounter.R
import com.example.pointcounter.databinding.AlertDialogMenuBinding
import com.example.pointcounter.model.entity.User
import com.example.pointcounter.viewmodel.SharedViewModel

class DialogMenu(private val viewModel: SharedViewModel) : DialogFragment() {

    private lateinit var dialogBinding: AlertDialogMenuBinding
    private var list = listOf<User>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogBinding = AlertDialogMenuBinding.inflate(LayoutInflater.from(context))
        return activity?.let { it ->
            val alertDialog = AlertDialog.Builder(it)
            alertDialog.setView(dialogBinding.root)

            viewModel.users.observe(this) { list = it }

            alertDialog.setPositiveButton(R.string.back_button)  { _, _ -> dismiss() }

            dialogBinding.dialogBtnResetScore.setOnClickListener {
                viewModel.resetAllUsersPoint(list)
            }

            alertDialog.create()

        }?: throw IllegalStateException("activity is null")
    }
}