package com.example.pointcounter.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.pointcounter.R
import com.example.pointcounter.databinding.AlertDialogDiceResultBinding
import com.example.pointcounter.viewmodel.SharedViewModel
import kotlin.properties.Delegates

class DialogDiceResult(private val viewModel: SharedViewModel) : DialogFragment() {

    private lateinit var dialogBinding: AlertDialogDiceResultBinding
    private var nbOfDice by Delegates.notNull<Int>()
    private val listOfResult = mutableListOf<Int>()
    private var totalResult = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogBinding = AlertDialogDiceResultBinding.inflate(LayoutInflater.from(context))
        return activity?.let { it ->
            val alertDialog = AlertDialog.Builder(it)
            alertDialog.setView(dialogBinding.root)



            viewModel.diceNumber.observe(requireActivity()) { nbOfDice = it }

            launchDice()

            dialogBinding.dialogImgDice.setOnClickListener { launchDice() }

            alertDialog.setNegativeButton(R.string.finish) {_,_ -> dismiss() }

            alertDialog.create()

        }?: throw IllegalStateException("activity is null")
    }

    private fun launchDice() {
        listOfResult.clear()
        totalResult = 0

        // SET RESULT
        viewModel.sideNumber.observe(requireActivity()) { nbOfSide ->
            for (i in 1..nbOfDice) {
                val result = viewModel.launchDice(nbOfSide)
                listOfResult.add(result)
                totalResult += result
            }
        }

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
        }

        // SET VIEW
        dialogBinding.apply {
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

        dialogBinding.tvTotalResult.text = totalResult.toString()

        setColorBtn()
    }

    private fun setColorBtn() {
        val color = viewModel.getRndColor()
        dialogBinding.dialogImgDice.setColorFilter(color)
        dialogBinding.textRoll.setTextColor(color)
        dialogBinding.tvTotal.setTextColor(color)
        dialogBinding.tvTotalResult.setTextColor(color)
    }
}