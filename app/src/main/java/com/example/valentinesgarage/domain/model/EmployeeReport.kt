package com.example.valentinesgarage.domain.model

data class EmployeeReport(
    val employee: Employee,
    val completedTasks: List<CompletedTaskDetail>,
    val servicedTruckCount: Int,
    val averageOdometerAtCheckIn: Double
)

