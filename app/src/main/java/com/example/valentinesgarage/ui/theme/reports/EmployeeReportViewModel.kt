package com.example.valentinesgarage.ui.theme.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.valentinesgarage.domain.model.Employee
import com.example.valentinesgarage.domain.model.EmployeeReport
import com.example.valentinesgarage.domain.repository.EmployeeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.util.Log

@HiltViewModel
class EmployeeReportViewModel @Inject constructor(
    private val employeeRepository: EmployeeRepository
) : ViewModel() {

    private val _employees = MutableStateFlow<List<Employee>>(emptyList())
    val employees: StateFlow<List<Employee>> = _employees

    private val _selectedEmployeeId = MutableStateFlow<String?>(null)
    val selectedEmployeeId: StateFlow<String?> = _selectedEmployeeId

    private val _report = MutableStateFlow<EmployeeReport?>(null)
    val report: StateFlow<EmployeeReport?> = _report



    fun selectEmployee(employeeId: String) {
        _selectedEmployeeId.value = employeeId
        viewModelScope.launch {
            try {
                val employeeReport = employeeRepository.getReportForEmployee(employeeId)
                _report.value = employeeReport
            } catch (e: Exception) {
                e.printStackTrace()
                // Optionally set an error state
                _report.value = null
            }
        }
    }
    fun loadEmployees() {
        viewModelScope.launch {
            try {
                val empList = employeeRepository.getAllEmployees()
                Log.d("EmployeeReportVM", "Loaded employees: $empList")
                _employees.value = empList
            } catch (e: Exception) {
                Log.e("EmployeeReportVM", "Failed to load employees", e)
            }
        }
    }
}