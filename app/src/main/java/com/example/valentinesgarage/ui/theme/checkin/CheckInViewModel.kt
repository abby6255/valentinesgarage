package com.example.valentinesgarage.ui.theme.checkin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.valentinesgarage.domain.model.Truck
import com.example.valentinesgarage.domain.usecase.CheckInTruckUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CheckInUiState(
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class CheckInViewModel @Inject constructor(
    private val checkInTruckUseCase: CheckInTruckUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckInUiState())
    val uiState: StateFlow<CheckInUiState> = _uiState

    fun checkIn(licensePlate: String, model: String, odometerKm: Int, conditionNotes: String) {
        viewModelScope.launch {
            _uiState.value = CheckInUiState(isLoading = true)
            val truck = Truck(
                licensePlate = licensePlate,
                model = model,
                odometerKm = odometerKm,
                conditionNotes = conditionNotes
            )
            val result = checkInTruckUseCase(truck)
            result.fold(
                onSuccess = { jobId ->
                    _uiState.value = CheckInUiState(success = true)
                },
                onFailure = { e ->
                    _uiState.value = CheckInUiState(error = e.message)
                }
            )
        }
    }
}