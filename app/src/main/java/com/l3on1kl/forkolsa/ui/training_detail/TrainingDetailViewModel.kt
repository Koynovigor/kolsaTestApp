package com.l3on1kl.forkolsa.ui.training_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.l3on1kl.forkolsa.domain.model.Video
import com.l3on1kl.forkolsa.domain.repository.TrainingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrainingDetailViewModel @Inject constructor(
    private val repository: TrainingRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val trainingId: Int = savedStateHandle["trainingId"] ?: -1

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    private fun load() = viewModelScope.launch {
        _uiState.value = DetailUiState.Loading
        try {
            val video = repository.getVideo(trainingId)
            _uiState.value = DetailUiState.Success(video)
        } catch (e: Exception) {
            _uiState.value = DetailUiState.Error("Не удалось загрузить видео")
        }
    }

    sealed class DetailUiState {
        data object Loading : DetailUiState()
        data class Success(val video: Video) : DetailUiState()
        data class Error(val message: String) : DetailUiState()
    }
}
