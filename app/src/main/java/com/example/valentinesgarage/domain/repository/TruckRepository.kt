package com.example.valentinesgarage.domain.repository

import com.example.valentinesgarage.data.model.TruckDto
import com.example.valentinesgarage.domain.model.Truck

interface TruckRepository {
    suspend fun checkInTruck(truck: Truck): Result<Unit>
    suspend fun getTruckById(truckId: String): Result<Truck>
    fun toDomain(truckDto: TruckDto): Truck
}