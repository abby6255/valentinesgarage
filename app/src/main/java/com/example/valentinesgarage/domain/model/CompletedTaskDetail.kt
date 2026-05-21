package com.example.valentinesgarage.domain.model

data class CompletedTaskDetail(
    val truckLicensePlate: String,
    val taskDescription: String,
    val completedAt: Long,
    val notes: String
)