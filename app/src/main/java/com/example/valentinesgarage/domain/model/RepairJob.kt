package com.example.valentinesgarage.domain.model

data class RepairJob(
    val jobId: String = "",
    val truckId: String,
    val status: JobStatus = JobStatus.CHECKED_IN,
    val tasks: List<RepairTask> = emptyList(),
    val overallNotes: String = ""
)



