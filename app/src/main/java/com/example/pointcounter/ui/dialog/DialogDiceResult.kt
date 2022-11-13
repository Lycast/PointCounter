package com.example.pointcounter.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.pointcounter.R
import com.example.pointcounter.databinding.AlertDialogDiceResultBinding
import com.example.pointcounter.viewmodel.SharedViewModel

class DialogDiceResult(private val viewModel: SharedViewModel, private val customDice: Boolean) : DialogFragment() {

    private lateinit var dialogBinding: AlertDialogDiceResultBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogBinding = AlertDialogDiceResultBinding.inflate(LayoutInflater.from(context))
        return activity?.let { it ->
            val alertDialog = AlertDialog.Builder(it)
            alertDialog.setView(dialogBinding.root)

            var color = viewModel.getRndColor()



            // Populate alert dialog
            dialogBinding.apply {
                viewModel.diceResult.observe(requireActivity()) { result ->
                    // set color dice
                    dialogImgDice.setColorFilter(color)
                    textRoll.setTextColor(color)

                    // set result
                    tvResultDice1.text = result.toString()

                    // check if dice is used
                    if (tvResultDice1.text.equals("")) card1.visibility = View.GONE
                    if (tvResultDice2.text.equals("")) card2.visibility = View.GONE
                    if (tvResultDice3.text.equals("")) card3.visibility = View.GONE
                    if (tvResultDice4.text.equals("")) card4.visibility = View.GONE
                    if (tvResultDice5.text.equals("")) card5.visibility = View.GONE
                    if (tvResultDice6.text.equals("")) card6.visibility = View.GONE
                    if (tvResultDice7.text.equals("")) card7.visibility = View.GONE
                    if (tvResultDice8.text.equals("")) card8.visibility = View.GONE
                    if (tvResultDice9.text.equals("")) card9.visibility = View.GONE
                    if (tvResultDice10.text.equals("")) card10.visibility = View.GONE
                }
            }

            // On click button roll dice
            dialogBinding.dialogImgDice.setOnClickListener {
                color = viewModel.getRndColor()
                viewModel.launchNormalDice()
            }

            // On click dice result text
            dialogBinding.tvResultDice1.setOnClickListener {
                color = viewModel.getRndColor()
                viewModel.launchNormalDice()
            }

            alertDialog.setNegativeButton(R.string.finish) {_,_ -> dismiss() }

            alertDialog.create()

        }?: throw IllegalStateException("activity is null")
    }
}