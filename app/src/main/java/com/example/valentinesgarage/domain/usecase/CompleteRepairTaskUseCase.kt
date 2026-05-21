package com.example.valentinesgarage.domain.usecase

import com.example.valentinesgarage.domain.model.Employee
import com.example.valentinesgarage.domain.model.JobStatus
import com.example.valentinesgarage.domain.model.RepairJob
import com.example.valentinesgarage.domain.repository.JobRepository

class CompleteRepairTaskUseCase(private val jobRepository: JobRepository) {
    suspend operator fun invoke(
        job: RepairJob,
        taskId: String,
        mechanic: Employee,
        notes: String = ""
    ): Result<RepairJob> {
        val updatedTasks = job.tasks.map { task ->
            if (task.taskId == taskId && !task.completed) {
                task.copy(
                    completed = true,
                    completedByEmployeeId = mechanic.id,
                    completedByEmployeeName = mechanic.name,
                    notes = task.notes + if (notes.isNotEmpty()) "\n$notes" else ""
                )
            } else task
        }
        val updatedJob = job.copy(tasks = updatedTasks, status = JobStatus.IN_PROGRESS)
        return jobRepository.updateJob(updatedJob).map { updatedJob }
    }
}