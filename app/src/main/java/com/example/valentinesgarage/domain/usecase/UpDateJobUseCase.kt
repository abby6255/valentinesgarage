package com.example.valentinesgarage.domain.usecase

import com.example.valentinesgarage.domain.model.RepairJob
import com.example.valentinesgarage.domain.repository.JobRepository

class UpdateJobUseCase(private val jobRepository: JobRepository) {
    suspend operator fun invoke(job: RepairJob): Result<Unit> {
        return jobRepository.updateJob(job)
    }
}