package com.example.pointcounter.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.pointcounter.R
import com.example.pointcounter.data.GuestRoomDatabase
import com.example.pointcounter.databinding.ActivityMainBinding
import com.example.pointcounter.databinding.PopupAddGuestBinding
import com.example.pointcounter.model.entity.Guest
import com.example.pointcounter.repository.Repository
import com.example.pointcounter.ui.adapter.AdapterEnum
import com.example.pointcounter.ui.adapter.GuestAdapter
import com.example.pointcounter.ui.adapter.OnItemClickListener
import com.example.pointcounter.viewmodel.SharedViewModel
import com.example.pointcounter.viewmodel.SharedViewModelFactory

class MainActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter : GuestAdapter
    private lateinit var viewModel: SharedViewModel
    private var numberOfGuest = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dao = GuestRoomDatabase.getInstance(application).guestDao
        val repository = Repository(dao)
        val factory = SharedViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[SharedViewModel::class.java]

        binding.btnAddGuest.setOnClickListener {
            // Open popup add guest
            setDisplayPopupAddGuest(null)
        }
        displayGuests()
        //setOnClickStartCounter()
    }

    private fun launchCounter() {
        if (binding.mainChipGuest1.isChecked) {
            numberOfGuest ++
            //todo récupérer le text pour l'ajouter a une liste de nom pour créer les compteur de points
        }
        if (binding.mainChipGuest2.isChecked) { numberOfGuest ++ }
        if (binding.mainChipGuest3.isChecked) { numberOfGuest ++ }
        if (binding.mainChipGuest4.isChecked) { numberOfGuest ++ }

        val intent = Intent(this@MainActivity, CounterActivity::class.java)
        intent.putExtra("nbOfGuest", numberOfGuest)
        startActivity(intent)
    }

//    private fun setOnClickStartCounter() {
//        binding.btnStartCounter.setOnClickListener {
//            launchCounter()
//        }
//    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.item_menu_delete_all) {
            // Action delete all
            viewModel.deleteAllGuest()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setDisplayPopupAddGuest(guest: Guest?) {
        val alertDialog = AlertDialog.Builder(this)
        val dialogBinding = PopupAddGuestBinding.inflate(layoutInflater)
        alertDialog.setView(dialogBinding.root)
        val dialog = alertDialog.create()

        dialogBinding.buttonAddNewGuest.setOnClickListener {
            if (guest == null) {
                if (!TextUtils.isEmpty(dialogBinding.editTextEnterName.text)) {
                    //  Add new guest
                    val newGuest = Guest(0, dialogBinding.editTextEnterName.text.toString())
                    viewModel.addGuest(newGuest)
                } else {
                    Toast.makeText(this, "The name must be filled in", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            } else {
                if (!TextUtils.isEmpty(dialogBinding.editTextEnterName.text)) {
                    // Update guest
                    viewModel.updateGuest( Guest(guest.id, dialogBinding.editTextEnterName.text.toString()))
                } else {
                    Toast.makeText(this, "The name must be filled in", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun displayGuests() {
        viewModel.guests.observe(this) {
            adapter = GuestAdapter(it, this)
            binding.recyclerViewGuests.adapter = adapter
        }
    }

    override fun setOnItemClickListener(guest: Guest, enum: AdapterEnum) {
        when ( enum ) {
            AdapterEnum.DELETE -> viewModel.deleteGuest(guest)
            AdapterEnum.EDIT -> setDisplayPopupAddGuest(guest)
            AdapterEnum.SELECT -> binding.mainChipGuest1.text = guest.name
            AdapterEnum.DESELECT -> binding.mainChipGuest1.text = resources.getText(R.string.guest_1)
        }
    }
}