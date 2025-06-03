package com.l3on1kl.forkolsa.ui.trainings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.l3on1kl.forkolsa.domain.repository.TrainingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrainingsViewModel @Inject constructor(
    private val repository: TrainingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<TrainingsUiState>(TrainingsUiState.Loading)
    val uiState: StateFlow<TrainingsUiState> = _uiState.asStateFlow()

    init {
        fetchTrainings()
    }

    private fun fetchTrainings() {
        viewModelScope.launch {
            _uiState.value = TrainingsUiState.Loading
            try {
                val list = repository.getTrainings()
                _uiState.value = TrainingsUiState.Success(list)
            } catch (e: Exception) {
                _uiState.value = TrainingsUiState.Error("Ошибка загрузки: ${e.localizedMessage}")
            }
        }
    }
}