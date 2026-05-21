package com.example.valentinesgarage.domain.usecase

import com.example.valentinesgarage.domain.model.EmployeeReport
import com.example.valentinesgarage.domain.repository.EmployeeRepository

class GetEmployeeReportUseCase(private val employeeRepository: EmployeeRepository) {
    suspend operator fun invoke(employeeId: String): EmployeeReport {
        return employeeRepository.getReportForEmployee(employeeId)
    }
}