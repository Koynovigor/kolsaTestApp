package com.l3on1kl.forkolsa.ui.list_trainings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.l3on1kl.forkolsa.databinding.FragmentTrainingsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TrainingsFragment : Fragment() {

    private var _binding: FragmentTrainingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TrainingsViewModel by viewModels()
    private val adapter = TrainingAdapter { training ->
        val action = TrainingsFragmentDirections
            .actionTrainingsFragmentToTrainingDetailFragment(
                trainingId = training.id,
                title = training.title,
                description = training.description,
                duration = training.duration
            )
        findNavController().navigate(action)
    }

    private var previousType: Int? = null
    private val typeOrder = listOf(null, 1, 2, 3)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrainingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecycler()
        setupSearch()
        setupFilterButtons()
        updateFilterButtonStates(viewModel.currentTypeFilter)

        binding.searchInput.setText(viewModel.currentQueryValue)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is TrainingsUiState.Loading -> {
                        binding.progressBar.isVisible = true
                        binding.emptyView.isVisible = false
                        binding.trainingsRecycler.isVisible = false
                    }

                    is TrainingsUiState.Success -> {
                        binding.progressBar.isVisible = false
                        val isEmpty = state.trainings.isEmpty()
                        binding.emptyView.isVisible = isEmpty
                        binding.trainingsRecycler.isVisible = !isEmpty

                        adapter.submitList(state.trainings)
                    }

                    is TrainingsUiState.Error -> {
                        binding.progressBar.isVisible = false
                        binding.emptyView.isVisible = false
                        binding.trainingsRecycler.isVisible = false
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        viewModel.loadTrainings()
    }

    private fun setupRecycler() {
        binding.trainingsRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.trainingsRecycler.adapter = adapter
    }

    private fun setupSearch() {
        binding.searchInput.doAfterTextChanged { editable ->
            viewModel.onSearchQueryChanged(editable?.toString().orEmpty())
        }
    }

    private fun setupFilterButtons() {
        binding.typeAll.isSelected = true

        val map = mapOf(
            binding.typeAll to null,
            binding.type1 to 1,
            binding.type2 to 2,
            binding.type3 to 3,
        )

        map.forEach { (button, type) ->
            button.setOnClickListener { handleTypeFilterChange(type) }
        }
    }

    private fun handleTypeFilterChange(newType: Int?) {
        animateDirection(previousType, newType)
        previousType = newType
        viewModel.onTypeFilterChanged(newType)
        updateFilterButtonStates(newType)
    }

    private fun animateDirection(old: Int?, new: Int?) {
        val oldPos = typeOrder.indexOf(old).coerceAtLeast(0)
        val newPos = typeOrder.indexOf(new).coerceAtLeast(0)
        if (oldPos == newPos) return

        val direction = if (newPos > oldPos) 1 else -1
        val list = binding.trainingsRecycler
        val width = list.width.toFloat()

        if (width == 0f) {
            list.post { animateDirection(old, new) }
            return
        }

        list.animate().cancel()
        list.translationX = width * direction
        list.animate()
            .translationX(0f)
            .setDuration(250)
            .setInterpolator(
                androidx.interpolator.view.animation.FastOutSlowInInterpolator()
            )
            .start()
    }


    private fun updateFilterButtonStates(selectedType: Int?) {
        val buttons = listOf(
            binding.typeAll to null,
            binding.type1 to 1,
            binding.type2 to 2,
            binding.type3 to 3
        )

        buttons.forEach { (button, type) ->
            button.isSelected = type == selectedType
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
