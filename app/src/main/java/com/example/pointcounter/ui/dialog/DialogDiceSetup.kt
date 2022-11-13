package com.example.pointcounter.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.pointcounter.R
import com.example.pointcounter.databinding.AlertDialogDiceSetupBinding
import com.example.pointcounter.databinding.AlertDialogMenuBinding
import com.example.pointcounter.model.entity.User
import com.example.pointcounter.viewmodel.SharedViewModel

class DialogDiceSetup(private val viewModel: SharedViewModel) : DialogFragment() {

    private lateinit var dialogBinding: AlertDialogDiceSetupBinding
    private var list = listOf<User>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogBinding = AlertDialogDiceSetupBinding.inflate(LayoutInflater.from(context))
        return activity?.let { it ->
            val alertDialog = AlertDialog.Builder(it)
            alertDialog.setView(dialogBinding.root)

            dialogBinding.seekbarDice.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    dialogBinding.textSeekbar.text = progress.toString()
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) { }
                override fun onStopTrackingTouch(seekBar: SeekBar?) { }
            })

            alertDialog.setPositiveButton(R.string.ok)  { _, _ -> dismiss() }

            alertDialog.create()

        }?: throw IllegalStateException("activity is null")
    }


}