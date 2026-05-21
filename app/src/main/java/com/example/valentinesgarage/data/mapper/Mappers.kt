package com.example.valentinesgarage.data.mapper

import com.example.valentinesgarage.data.model.TruckDto
import com.example.valentinesgarage.data.model.RepairJobDto
import com.example.valentinesgarage.data.model.RepairTaskDto
import com.example.valentinesgarage.domain.model.*

fun TruckDto.toDomain() = Truck(
    id = id,
    licensePlate = licensePlate,
    model = model,
    odometerKm = odometerKm,
    conditionNotes = conditionNotes,
    checkInTimestamp = checkInTimestamp
)

fun Truck.toDto() = TruckDto(
    id = id,
    licensePlate = licensePlate,
    model = model,
    odometerKm = odometerKm,
    conditionNotes = conditionNotes,
    checkInTimestamp = checkInTimestamp
)

fun RepairJobDto.toDomain() = RepairJob(
    jobId = jobId,
    truckId = truckId,
    status = JobStatus.valueOf(status),
    tasks = tasks.map { it.toDomain() },
    overallNotes = overallNotes
)

fun RepairJob.toDto() = RepairJobDto(
    jobId = jobId,
    truckId = truckId,
    status = status.name,
    tasks = tasks.map { it.toDto() },
    overallNotes = overallNotes
)

fun RepairTaskDto.toDomain() = RepairTask(
    taskId = taskId,
    description = description,
    completed = completed,
    completedByEmployeeId = completedByEmployeeId,
    completedByEmployeeName = completedByEmployeeName,
    notes = notes
)

fun RepairTask.toDto() = RepairTaskDto(
    taskId = taskId,
    description = description,
    completed = completed,
    completedByEmployeeId = completedByEmployeeId,
    completedByEmployeeName = completedByEmployeeName,
    notes = notes
)