package com.example.valentinesgarage.domain.repository

import com.example.valentinesgarage.domain.model.RepairJob
import kotlinx.coroutines.flow.Flow

interface JobRepository {
    fun getActiveJobsFlow(): Flow<List<RepairJob>>
    suspend fun getJobById(jobId: String): Result<RepairJob>
    suspend fun updateJob(job: RepairJob): Result<Unit>
    suspend fun createJobForTruck(truckId: String): Result<String> // returns jobId
}