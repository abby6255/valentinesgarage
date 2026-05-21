package com.example.valentinesgarage.domain.usecase

import com.example.valentinesgarage.domain.model.Employee
import com.example.valentinesgarage.domain.repository.EmployeeRepository

class GetEmployeesUseCase(private val employeeRepository: EmployeeRepository) {
    suspend operator fun invoke(): List<Employee> {
        return employeeRepository.getAllEmployees()
    }
}