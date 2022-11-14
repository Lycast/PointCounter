package com.example.scorecounter.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.scorecounter.R
import com.example.scorecounter.databinding.AlertDialogDiceSetupBinding
import com.example.scorecounter.viewmodel.SharedViewModel

class DialogDiceSetup(private val viewModel: SharedViewModel) : DialogFragment() {

    private lateinit var dialogBinding: AlertDialogDiceSetupBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogBinding = AlertDialogDiceSetupBinding.inflate(LayoutInflater.from(context))
        return activity?.let { it ->
            val alertDialog = AlertDialog.Builder(it)
            alertDialog.setView(dialogBinding.root)

            // NUMBER DICE
            viewModel.diceNumber.observe(requireActivity()) {
                dialogBinding.seekbarDice.progress = it
                dialogBinding.tvSeekbarDice.text = it.toString()
                dialogBinding.seekbarDice.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                        dialogBinding.tvSeekbarDice.text = progress.toString()
                        viewModel.setDiceNumber(progress)
                    }
                    override fun onStartTrackingTouch(seekBar: SeekBar?) { }
                    override fun onStopTrackingTouch(seekBar: SeekBar?) { }
                })
            }

            // NUMBER SIDES
            viewModel.sideNumber.observe(requireActivity()) {
                dialogBinding.tvSeekbarSide.text = it.toString()
                dialogBinding.seekbarSide.afterProgressChanged { progress ->
                    dialogBinding.tvSeekbarSide.text = progress.toString()
                    viewModel.setSideNumber(progress)
                }
            }

            dialogBinding.etNbSide.afterTextChanged {
                if (it == "") dialogBinding.seekbarSide.progress = 1
                    else dialogBinding.seekbarSide.progress = it.toInt()
            }

            alertDialog.setPositiveButton(R.string.ok)  { _, _ -> dismiss() }

            alertDialog.create()

        }?: throw IllegalStateException("activity is null")
    }

    private fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(editable: Editable?) { afterTextChanged.invoke(editable.toString()) }
        })
    }

    private fun SeekBar.afterProgressChanged(afterProgressChanged: (Int) -> Unit) {
        this.setOnSeekBarChangeListener(object  : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                afterProgressChanged.invoke(progress) }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                dialogBinding.etNbSide.clearFocus()
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                dialogBinding.etNbSide.setText("$progress")
            }
        })
    }
}