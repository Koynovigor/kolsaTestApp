package com.l3on1kl.forkolsa.ui.trainings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
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
    private val adapter = TrainingAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrainingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.trainingsRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.trainingsRecycler.adapter = adapter

        lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is TrainingsUiState.Loading -> {
                        // TODO: loading UI
                    }

                    is TrainingsUiState.Success -> {
                        adapter.submitList(state.trainings)
                    }

                    is TrainingsUiState.Error -> {
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
