package com.example.pointcounter.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.KeyboardShortcutGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.pointcounter.R
import com.example.pointcounter.databinding.AlertDialogDiceSetupBinding
import com.example.pointcounter.viewmodel.SharedViewModel

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
                dialogBinding.textSeekbar.text = it.toString()
                dialogBinding.seekbarDice.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                        dialogBinding.textSeekbar.text = progress.toString()
                        viewModel.setDiceNumber(progress)
                    }
                    override fun onStartTrackingTouch(seekBar: SeekBar?) { }
                    override fun onStopTrackingTouch(seekBar: SeekBar?) { }
                })

                Log.e("MY-LOG", "dice number = $it")
            }



            // NUMBER SIDES
            viewModel.sideNumber.observe(requireActivity()) {
                dialogBinding.textSeekbarSide.text = it.toString()
                dialogBinding.seekbarSide.afterProgressChanged { progress ->
                    dialogBinding.textSeekbarSide.text = progress.toString()
                    dialogBinding.etNbSide.hint = progress.toString()
                    if (!dialogBinding.etNbSide.isFocused) dialogBinding.etNbSide.setText("")
                    viewModel.setSideNumber(progress)
                }
                Log.e("MY-LOG", "side number = $it")
            }

            dialogBinding.etNbSide.afterTextChanged {
                if (dialogBinding.etNbSide.text.toString() != "")
                    dialogBinding.seekbarSide.progress = dialogBinding.etNbSide.text.toString().toInt()
            }

            dialogBinding.etNbSide.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                    if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                        dialogBinding.etNbSide.clearFocus()
                        dialogBinding.etNbSide.isEnabled = false
                        return@OnKeyListener true
                    }
                    false
                })

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
                afterProgressChanged.invoke(progress)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
    }
}