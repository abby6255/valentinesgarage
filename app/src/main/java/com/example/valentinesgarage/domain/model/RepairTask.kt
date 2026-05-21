package com.example.valentinesgarage.domain.model

data class RepairTask(
    val taskId: String = "",
    val description: String,
    val completed: Boolean = false,
    val completedByEmployeeId: String? = null,
    val completedByEmployeeName: String? = null,
    val notes: String = ""
)