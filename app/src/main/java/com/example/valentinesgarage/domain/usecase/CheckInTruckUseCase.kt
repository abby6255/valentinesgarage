package com.example.valentinesgarage.domain.usecase

import com.example.valentinesgarage.domain.model.Truck
import com.example.valentinesgarage.domain.repository.JobRepository
import com.example.valentinesgarage.domain.repository.TruckRepository

class CheckInTruckUseCase(
    private val truckRepository: TruckRepository,
    private val jobRepository: JobRepository
) {
    suspend operator fun invoke(truck: Truck): Result<String> {
        return truckRepository.checkInTruck(truck).fold(
            onSuccess = {
                val jobResult = jobRepository.createJobForTruck(truck.id)
                jobResult
            },
            onFailure = { Result.failure(it) }
        )
    }
}