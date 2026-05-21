package com.example.valentinesgarage.ui.theme.jobs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.valentinesgarage.domain.model.RepairJob
import com.example.valentinesgarage.domain.usecase.GetActiveJobsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ActiveJobsViewModel @Inject constructor(
    private val getActiveJobsUseCase: GetActiveJobsUseCase
) : ViewModel() {

    val activeJobs: StateFlow<List<RepairJob>> = getActiveJobsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun loadActiveJobs() {
        // The flow is already active; this is just a placeholder to satisfy the call.
    }
}