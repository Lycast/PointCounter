package com.example.scorecounter.ui.navigation

import android.app.AlertDialog
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.scorecounter.R
import com.example.scorecounter.databinding.FragmentListBinding
import com.example.scorecounter.model.entity.User
import com.example.scorecounter.ui.adapter.list.RVListAdapter
import com.example.scorecounter.ui.dialog.DialogParticipant
import com.example.scorecounter.utils.OnItemClickListener
import com.example.scorecounter.utils.EnumItem
import com.example.scorecounter.viewmodel.SharedViewModel

class ListFragment : Fragment(), OnItemClickListener {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapterAll : RVListAdapter
    private lateinit var adapterSelected: RVListAdapter
    val viewModel by activityViewModels<SharedViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.liveDataListUser.observe(requireActivity()) {
            viewModel.updateListUnselected()
        }

        initRecyclerViewAll()
        initRecyclerViewSelected()
    }

    private fun setDataUnselected() {
        viewModel.listUserUnselected.observe(requireActivity()) {
            adapterAll.setDataAll(it)
        }
    }

    private fun setDataSelected() {
        viewModel.listUserSelected.observe(requireActivity()) { adapterSelected.setDataSelected(it) }
    }

    private fun initRecyclerViewAll() {
        adapterAll = RVListAdapter(this, true)
        binding.rvListCounter.adapter = adapterAll

        var column = 1
        if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) { column = 2 }
        binding.rvListCounter.layoutManager = GridLayoutManager( requireActivity(), column)

        setDataUnselected()
    }

    private fun initRecyclerViewSelected() {
        adapterSelected = RVListAdapter(this, false)
        binding.rvSelected.adapter = adapterSelected

        var column = 2
        if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) { column = 4 }
        binding.rvSelected.layoutManager = GridLayoutManager( requireActivity(), column)

        setDataSelected()
    }



    override fun setOnItemClickListener(user: User, enum: EnumItem, pos: Int?) {
        viewModel.currentUser = user
        when (enum) {
            EnumItem.DELETE -> {
                val alertDialog = AlertDialog.Builder(context)
                alertDialog.setTitle(R.string.delete_counter)
                alertDialog.setPositiveButton(R.string.yes) { _, _ ->
                    viewModel.deleteUser(user)
                }
                alertDialog.setNegativeButton(R.string.no) {_,_ -> alertDialog.create().dismiss()}
                alertDialog.create().show()
            }
            EnumItem.EDIT -> DialogParticipant().show(parentFragmentManager, "dialog_user")
            EnumItem.SELECT -> viewModel.addSelectedUser(user.id)
            EnumItem.UNSELECT -> viewModel.removeSelectedUser(user.id)
            else -> {}
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}