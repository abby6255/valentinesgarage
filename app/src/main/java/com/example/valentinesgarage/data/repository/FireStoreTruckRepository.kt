package com.example.valentinesgarage.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.example.valentinesgarage.data.mapper.toDto
import com.example.valentinesgarage.domain.model.Truck
import com.example.valentinesgarage.domain.repository.TruckRepository
import kotlinx.coroutines.tasks.await
import com.example.valentinesgarage.data.model.TruckDto

 class FirestoreTruckRepository(private val firestore: FirebaseFirestore) : TruckRepository {

    override suspend fun checkInTruck(truck: Truck): Result<Unit> {
        return try {
            val docRef = firestore.collection("trucks").document()
            val dto = truck.toDto().copy(id = docRef.id)
            docRef.set(dto).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTruckById(truckId: String): Result<Truck> {
        return try {
            val doc = firestore.collection("trucks").document(truckId).get().await()
            val truckDto = doc.toObject(TruckDto::class.java) ?: throw Exception("Truck not found")
            Result.success(toDomain(truckDto))//error
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

     override fun toDomain(truckDto: TruckDto): Truck {
         TODO("Not yet implemented")
     }
 }