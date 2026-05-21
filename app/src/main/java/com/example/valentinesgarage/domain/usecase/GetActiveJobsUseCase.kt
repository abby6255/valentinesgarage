package com.example.valentinesgarage.domain.usecase

import com.example.valentinesgarage.domain.model.RepairJob
import com.example.valentinesgarage.domain.repository.JobRepository
import kotlinx.coroutines.flow.Flow

class GetActiveJobsUseCase(private val jobRepository: JobRepository) {
    operator fun invoke(): Flow<List<RepairJob>> = jobRepository.getActiveJobsFlow()
}