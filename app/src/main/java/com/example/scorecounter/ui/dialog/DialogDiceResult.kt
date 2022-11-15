package com.example.scorecounter.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.scorecounter.R
import com.example.scorecounter.databinding.AlertDialogDiceResultBinding
import com.example.scorecounter.viewmodel.SharedViewModel
import kotlin.properties.Delegates

class DialogDiceResult : DialogFragment() {

    private lateinit var dialogBinding: AlertDialogDiceResultBinding
    private var nbOfDice by Delegates.notNull<Int>()
    private var totalResult = 0

    val viewModel by activityViewModels<SharedViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogBinding = AlertDialogDiceResultBinding.inflate(layoutInflater)
        return activity?.let { it ->
            val alertDialog = AlertDialog.Builder(it)
            alertDialog.setView(dialogBinding.root)

            // SETUP DICE LAUNCHER
            viewModel.listDicesResult.observe(requireActivity()) { setText(it) }
            viewModel.diceNumber.observe(requireActivity()) { nbOfDice = it }
            dialogBinding.dialogImgDice.setOnClickListener { launchDice(viewModel) }
            setView()

            alertDialog.setNegativeButton(R.string.customize) {_,_ ->
                viewModel.updateListDicesResult(listOf())
                DialogDiceSetup().show(parentFragmentManager, "dialog_dice_setup")
            }
            alertDialog.setPositiveButton(R.string.finish) {_,_ -> dismiss() }
            alertDialog.create()

        }?: throw IllegalStateException("activity is null")
    }

    private fun launchDice(viewModel: SharedViewModel) {

        viewModel.sideNumber.observe(requireActivity()) { nbOfSide ->
            val newListOfResult = mutableListOf<Int>()
            for (i in 1..nbOfDice) {
                val dice = viewModel.launchDice(nbOfSide)
                newListOfResult.add(dice)
            }
            viewModel.updateListDicesResult(newListOfResult)
        }


        setView()
        setColorBtn()
    }

    private fun setColorBtn() {
        val color = viewModel.getRndColor()
        dialogBinding.dialogImgDice.setColorFilter(color)
        dialogBinding.tvRollAgain.setTextColor(color)
        dialogBinding.tvTotal.setTextColor(color)
        dialogBinding.tvTotalResult.setTextColor(color)
    }

    private fun setText(listOfResult: List<Int>) {
        totalResult = 0
        for (i in listOfResult) totalResult += i

        // SET TEXT
        dialogBinding.apply {
            if (listOfResult.isNotEmpty()) tvResultDice1.text = listOfResult[0].toString()
            if (listOfResult.size >= 2) tvResultDice2.text = listOfResult[1].toString()
            if (listOfResult.size >= 3) tvResultDice3.text = listOfResult[2].toString()
            if (listOfResult.size >= 4) tvResultDice4.text = listOfResult[3].toString()
            if (listOfResult.size >= 5) tvResultDice5.text = listOfResult[4].toString()
            if (listOfResult.size >= 6) tvResultDice6.text = listOfResult[5].toString()
            if (listOfResult.size >= 7) tvResultDice7.text = listOfResult[6].toString()
            if (listOfResult.size >= 8) tvResultDice8.text = listOfResult[7].toString()
            if (listOfResult.size >= 9) tvResultDice9.text = listOfResult[8].toString()
            if (listOfResult.size >= 10) tvResultDice10.text = listOfResult[9].toString()

            tvTotalResult.text = totalResult.toString()
        }
    }

    private fun setView() {
        dialogBinding.apply {
            // check if dice is used
            if (tvResultDice1.text.equals("")) card1.visibility = View.GONE
            else card1.visibility = View.VISIBLE
            if (tvResultDice2.text.equals("")) card2.visibility = View.GONE
            else card2.visibility = View.VISIBLE
            if (tvResultDice3.text.equals("")) card3.visibility = View.GONE
            else card3.visibility = View.VISIBLE
            if (tvResultDice4.text.equals("")) card4.visibility = View.GONE
            else card4.visibility = View.VISIBLE
            if (tvResultDice5.text.equals("")) card5.visibility = View.GONE
            else card5.visibility = View.VISIBLE
            if (tvResultDice6.text.equals("")) card6.visibility = View.GONE
            else card6.visibility = View.VISIBLE
            if (tvResultDice7.text.equals("")) card7.visibility = View.GONE
            else card7.visibility = View.VISIBLE
            if (tvResultDice8.text.equals("")) card8.visibility = View.GONE
            else card8.visibility = View.VISIBLE
            if (tvResultDice9.text.equals("")) card9.visibility = View.GONE
            else card9.visibility = View.VISIBLE
            if (tvResultDice10.text.equals("")) card10.visibility = View.GONE
            else card10.visibility = View.VISIBLE
        }
    }
}