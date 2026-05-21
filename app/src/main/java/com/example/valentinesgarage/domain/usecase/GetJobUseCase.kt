package com.example.valentinesgarage.domain.usecase

import com.example.valentinesgarage.domain.model.RepairJob
import com.example.valentinesgarage.domain.repository.JobRepository

class GetJobUseCase(private val jobRepository: JobRepository) {
    suspend operator fun invoke(jobId: String): Result<RepairJob> {
        return jobRepository.getJobById(jobId)
    }
}