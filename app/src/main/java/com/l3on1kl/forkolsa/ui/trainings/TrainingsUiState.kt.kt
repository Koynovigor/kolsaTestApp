package com.l3on1kl.forkolsa.ui.trainings

import com.l3on1kl.forkolsa.domain.model.Training

sealed class TrainingsUiState {
    data object Loading : TrainingsUiState()

    data class Success(val trainings: List<Training>) : TrainingsUiState()

    data class Error(val message: String) : TrainingsUiState()

}
