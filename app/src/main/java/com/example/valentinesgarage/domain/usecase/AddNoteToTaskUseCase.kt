package com.example.valentinesgarage.domain.usecase

import com.example.valentinesgarage.domain.model.RepairJob
import com.example.valentinesgarage.domain.repository.JobRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class AddNoteToTaskUseCase @Inject constructor(
    private val jobRepository: JobRepository
) {
    /**
     * Adds a note to a specific task within a repair job.
     * If the task is already completed, the note is still appended.
     *
     * @param jobId The ID of the repair job
     * @param taskId The ID of the task to update
     * @param note The note text to append (will be added on a new line)
     * @return Result<Unit> indicating success or failure
     */
    suspend operator fun invoke(jobId: String, taskId: String, note: String): Result<Unit> {
        return try {
            // Fetch the current job
            val jobResult = jobRepository.getJobById(jobId)
            val job = jobResult.getOrElse { return Result.failure(it) }

            // Find and update the task
            val updatedTasks = job.tasks.map { task ->
                if (task.taskId == taskId) {
                    val newNotes = if (task.notes.isBlank()) {
                        note
                    } else {
                        "${task.notes}\n$note"
                    }
                    task.copy(notes = newNotes)
                } else {
                    task
                }
            }

            val updatedJob = job.copy(tasks = updatedTasks)
            jobRepository.updateJob(updatedJob)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}