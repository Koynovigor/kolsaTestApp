package com.l3on1kl.forkolsa.ui.list_trainings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.l3on1kl.forkolsa.domain.model.Training
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
    private var allTrainings: List<Training> = emptyList()
    private var currentQuery: String = ""

    var currentTypeFilter: Int? = null
    val uiState: StateFlow<TrainingsUiState> = _uiState.asStateFlow()
    val currentQueryValue: String get() = currentQuery

    fun loadTrainings() {
        viewModelScope.launch {
            _uiState.value = TrainingsUiState.Loading
            try {
                allTrainings = repository.getTrainings()
                applyFilters()
            } catch (e: Exception) {
                _uiState.value = TrainingsUiState.Error("Ошибка загрузки данных")
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        currentQuery = query
        applyFilters()
    }

    fun onTypeFilterChanged(type: Int?) {
        currentTypeFilter = type
        applyFilters()
    }

    private fun applyFilters() {
        val filtered = allTrainings.filter { training ->
            (currentTypeFilter == null || training.type == currentTypeFilter) &&
                    (currentQuery.isBlank() || training.title.contains(
                        currentQuery,
                        ignoreCase = true
                    ))
        }
        _uiState.value = TrainingsUiState.Success(filtered)
    }
}
