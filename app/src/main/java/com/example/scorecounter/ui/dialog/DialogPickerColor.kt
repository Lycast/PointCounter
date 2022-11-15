package com.example.scorecounter.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.scorecounter.R
import com.example.scorecounter.databinding.AlertDialogPickColorBinding
import com.example.scorecounter.viewmodel.SharedViewModel

class DialogPickerColor : DialogFragment() {

    private lateinit var dialogBinding: AlertDialogPickColorBinding
    val viewModel by activityViewModels<SharedViewModel>()
    private var red: Int = 0
    private var green: Int = 0
    private var blue: Int = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogBinding = AlertDialogPickColorBinding.inflate(layoutInflater)
        return activity?.let {
            val alertDialog = AlertDialog.Builder(it)
            alertDialog.setView(dialogBinding.root)

            alertDialog.setPositiveButton(R.string.ok) { _, _ -> dismiss() }

            viewModel.color.observe(this) { color ->
                dialogBinding.colorCard.setBackgroundColor(color)
                red = color.red
                green = color.green
                blue = color.blue

                dialogBinding.seekbarBlue.progress = blue
                dialogBinding.seekbarGreen.progress = green
                dialogBinding.seekbarRed.progress = red
            }

            dialogBinding.seekbarBlue.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    blue = progress
                    viewModel.setColor(red, green, blue)
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) { }
                override fun onStopTrackingTouch(seekBar: SeekBar?) { }
            })

            dialogBinding.seekbarGreen.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    green = progress
                    viewModel.setColor(red, green, blue)
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) { }
                override fun onStopTrackingTouch(seekBar: SeekBar?) { }
            })

            dialogBinding.seekbarRed.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    red = progress
                    viewModel.setColor(red, green, blue)
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) { }
                override fun onStopTrackingTouch(seekBar: SeekBar?) { }
            })

            alertDialog.create()
        }?: throw IllegalStateException("activity is null")
    }
}