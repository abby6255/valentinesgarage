package com.example.valentinesgarage.ui.theme.jobs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.valentinesgarage.domain.model.Employee
import com.example.valentinesgarage.domain.model.RepairJob
import com.example.valentinesgarage.domain.model.JobStatus
import com.example.valentinesgarage.domain.repository.EmployeeRepository
import com.example.valentinesgarage.domain.usecase.AddNoteToTaskUseCase
import com.example.valentinesgarage.domain.repository.JobRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class JobDetailUiState(
    val job: RepairJob? = null,
    val currentEmployee: Employee? = null, // In real app, get from auth
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class JobDetailViewModel @Inject constructor(
    private val jobRepository: JobRepository,
    private val employeeRepository: EmployeeRepository, // to get current user info
    private val addNoteToTaskUseCase: AddNoteToTaskUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(JobDetailUiState())
    val uiState: StateFlow<JobDetailUiState> = _uiState

    fun loadJob(jobId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val jobResult = jobRepository.getJobById(jobId)
            jobResult.fold(
                onSuccess = { job ->
                    // For demo, assume first employee from list
                    val employees = employeeRepository.getAllEmployees()
                    val currentEmp = employees.firstOrNull()
                    _uiState.value = JobDetailUiState(job = job, currentEmployee = currentEmp)
                },
                onFailure = { e ->
                    _uiState.value = JobDetailUiState(error = e.message)
                }
            )
        }
    }

    fun toggleTaskCompleted(taskId: String, notes: String) {
        val currentJob = _uiState.value.job ?: return
        val employee = _uiState.value.currentEmployee ?: return
        // Prevent modifying already completed tasks
        val updatedTasks = currentJob.tasks.map { task ->
            if (task.taskId == taskId && !task.completed) {
                task.copy(
                    completed = true,
                    completedByEmployeeId = employee.id,
                    completedByEmployeeName = employee.name,
                    notes = task.notes + if (notes.isNotEmpty()) "\n$notes" else ""
                )
            } else task
        }
        val updatedJob = currentJob.copy(tasks = updatedTasks, status = JobStatus.IN_PROGRESS)
        viewModelScope.launch {
            jobRepository.updateJob(updatedJob).fold(
                onSuccess = { _uiState.value = _uiState.value.copy(job = updatedJob) },
                onFailure = { e -> _uiState.value = _uiState.value.copy(error = e.message) }
            )
        }
    }

    fun addNoteToTask(taskId: String, note: String) {
        // Similar to toggle but without marking completed
        val currentJob = _uiState.value.job ?: return
        val updatedTasks = currentJob.tasks.map { task ->
            if (task.taskId == taskId) {
                task.copy(notes = task.notes + if (note.isNotEmpty()) "\n$note" else "")
            } else task
        }
        val updatedJob = currentJob.copy(tasks = updatedTasks)
        viewModelScope.launch {
            jobRepository.updateJob(updatedJob).fold(
                onSuccess = { _uiState.value = _uiState.value.copy(job = updatedJob) },
                onFailure = { e -> _uiState.value = _uiState.value.copy(error = e.message) }
            )
        }
    }
}



