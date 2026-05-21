package com.example.valentinesgarage.data.model

data class RepairJobDto(
    var jobId: String = "",
    val truckId: String = "",
    val status: String = "CHECKED_IN", // Stored as string for simplicity
    val tasks: List<RepairTaskDto> = emptyList(),
    val overallNotes: String = ""
)

data class RepairTaskDto(
    val taskId: String = "",
    val description: String = "",
    val completed: Boolean = false,
    val completedByEmployeeId: String? = null,
    val completedByEmployeeName: String? = null,
    val notes: String = ""
)