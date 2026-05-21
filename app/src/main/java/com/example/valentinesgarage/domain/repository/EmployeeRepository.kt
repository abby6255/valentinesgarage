package com.example.valentinesgarage.domain.repository

import com.example.valentinesgarage.domain.model.Employee
import com.example.valentinesgarage.domain.model.EmployeeReport

data class EmployeeReport(
    val employee: Employee,
    val completedTasks: List<CompletedTaskDetail>,
    val servicedTruckCount: Int,
    val averageOdometerAtCheckIn: Double
)

data class CompletedTaskDetail(
    val truckLicensePlate: String,
    val taskDescription: String,
    val completedAt: Long,
    val notes: String
)

interface EmployeeRepository {
    suspend fun getAllEmployees(): List<Employee>
    suspend fun getReportForEmployee(employeeId: String): EmployeeReport
}