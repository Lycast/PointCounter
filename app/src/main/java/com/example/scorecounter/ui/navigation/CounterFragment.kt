package com.example.scorecounter.ui.navigation

import android.app.AlertDialog
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.scorecounter.R
import com.example.scorecounter.databinding.FragmentCounterBinding
import com.example.scorecounter.databinding.ToolbarLayoutStepBinding
import com.example.scorecounter.model.entity.User
import com.example.scorecounter.ui.adapter.counter.RVCountAdapter
import com.example.scorecounter.ui.dialog.DialogInputScore
import com.example.scorecounter.ui.dialog.DialogInputStep
import com.example.scorecounter.ui.dialog.DialogParticipant
import com.example.scorecounter.utils.EnumItem
import com.example.scorecounter.utils.EnumVHSelect
import com.example.scorecounter.utils.OnItemClickListener
import com.example.scorecounter.viewmodel.SharedViewModel


class CounterFragment : Fragment(), OnItemClickListener {

    private var _binding: FragmentCounterBinding? = null
    private val binding get() = _binding!!
    val viewModel by activityViewModels<SharedViewModel>()

    private lateinit var stepBinding: ToolbarLayoutStepBinding
    private var step = 1

    private lateinit var adapter : RVCountAdapter
    private var listAdapter: List<User> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCounterBinding.inflate(inflater, container, false)
        stepBinding = ToolbarLayoutStepBinding.bind(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            binding.llCounters.orientation = LinearLayout.HORIZONTAL

        initRecyclerView()
        initObserverViewModel()

        binding.ivAddCount.setOnClickListener { viewModel.addUser(User(0,viewModel.getRndName(),0,viewModel.getRndColor())) }
    }

    override fun setOnItemClickListener(user: User, enum: EnumItem, pos: Int) {
        viewModel.currentUser = user
        when (enum) {
            EnumItem.RESET_POINT -> {
                user.score = 0
                viewModel.updateUser(user) }
            EnumItem.ADD_POINT -> {
                user.score += step
                viewModel.updateUser(user) }
            EnumItem.REMOVE_POINT -> {
                user.score -= step
                viewModel.updateUser(user) }
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
            EnumItem.SET_POINT -> DialogInputScore().show(parentFragmentManager, "dialog_score")
            else -> {}
        }
    }

    private fun initRecyclerView() {
        viewModel.rvSize.observe(requireActivity()) {
            if (it > 0) {
                binding.llCounters.visibility = View.GONE
                binding.rvCounters.visibility = View.VISIBLE
            }
            if (it == 1) setRVSize1()
            if (it == 2) setRVSize2()
            if (it == 3) setRVSize3()
        }
    }

    private fun setRVSize1() {
        adapter = RVCountAdapter(this, EnumVHSelect.SIZE_1)
        binding.rvCounters.adapter = adapter
        var column = 1
        if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) { column = 2 }
        binding.rvCounters.layoutManager = GridLayoutManager( requireActivity(), column)
        setData()
    }

    private fun setRVSize2() {
        adapter = RVCountAdapter(this, EnumVHSelect.SIZE_2)
        binding.rvCounters.adapter = adapter
        var column = 1
        if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) { column = 2 }
        binding.rvCounters.layoutManager = GridLayoutManager( requireActivity(), column)
        setData()
    }

    private fun setRVSize3() {
        adapter = RVCountAdapter(this, EnumVHSelect.SIZE_3)
        binding.rvCounters.adapter = adapter
        var column = 2
        if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) { column = 4 }
        binding.rvCounters.layoutManager = GridLayoutManager( requireActivity(), column)
        setData()
    }

    private fun setData() {
        viewModel.users.observe(requireActivity()) {
            adapter.setData(it)
        }
    }

    private fun initObserverViewModel() {
        viewModel.step.observe(requireActivity()) {
            step = it
            setToolbarStep()
        }
        viewModel.users.observe(requireActivity()) {
            listAdapter = it
            if ( it.isEmpty()) {
                binding.llCounters.visibility = View.GONE
                binding.ivAddCount.visibility = View.VISIBLE
            } else binding.ivAddCount.visibility = View.GONE

            if ( it.size in 1..2) setCounter1(it[0])
            if ( it.size == 2) setCounter2(it[1])
            if ( it.size < 3) viewModel.updateRVSize(0)
            if ( it.size in 3..8) viewModel.updateRVSize(1)
            if ( it.size in 9..15) viewModel.updateRVSize(2)
            if ( it.size > 15) viewModel.updateRVSize(3)
        }
    }

    private fun setToolbarStep() {

            stepBinding.step1.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.opacity_0))
            stepBinding.step5.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.opacity_0))
            stepBinding.step10.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.opacity_0))
            stepBinding.step25.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.opacity_0))
            stepBinding.stepSetup.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.opacity_0))

            when(step) {
                1 -> stepBinding.step1.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.light_gray))
                5 -> stepBinding.step5.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.light_gray))
                10 -> stepBinding.step10.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.light_gray))
                25 -> stepBinding.step25.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.light_gray))
                else -> {
                    stepBinding.stepSetup.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.light_gray))
            }
        }

        stepBinding.apply {
            step1.setOnClickListener { viewModel.setStep(1) }
            step5.setOnClickListener { viewModel.setStep(5) }
            step10.setOnClickListener { viewModel.setStep(10) }
            step25.setOnClickListener { viewModel.setStep(25) }
            stepSetup.setOnClickListener { DialogInputStep().show(parentFragmentManager, "dialog_step") }
        }
    }

    private fun setCounter1(user: User) {
        binding.llCounters.visibility = View.VISIBLE
        binding.rvCounters.visibility = View.GONE
        binding.llCounter2.visibility = View.GONE

        binding.itemTvName1.text = user.name
        binding.itemTvScore1.text = user.score.toString()
        binding.itemCard1.setCardBackgroundColor(user.color)
        binding.itemIvAdd1.setOnClickListener {
            user.score++
            viewModel.updateUser(user) }
        binding.itemIvRemove1.setOnClickListener {
            user.score--
            viewModel.updateUser(user) }
        binding.itemTvScore1.setOnClickListener {
            viewModel.currentUser = user
            DialogInputScore().show(parentFragmentManager, "dialog_input_score")
        }
        binding.itemTvName1.setOnClickListener {
            viewModel.currentUser = user
            DialogParticipant().show(parentFragmentManager, "dialog_participant")
        }
    }

    private fun setCounter2(user: User) {

        binding.llCounter2.visibility = View.VISIBLE
        binding.itemTvName2.text = user.name
        binding.itemTvScore2.text = user.score.toString()
        binding.itemCard2.setCardBackgroundColor(user.color)
        binding.itemIvAdd2.setOnClickListener {
            user.score++
            viewModel.updateUser(user) }
        binding.itemIvRemove2.setOnClickListener {
            user.score--
            viewModel.updateUser(user) }
        binding.itemTvScore2.setOnClickListener {
            viewModel.currentUser = user
            DialogInputScore().show(parentFragmentManager, "dialog_input_score") }
        binding.itemTvName2.setOnClickListener {
            viewModel.currentUser = user
            DialogParticipant().show(parentFragmentManager, "dialog_participant")
        }
    }
}