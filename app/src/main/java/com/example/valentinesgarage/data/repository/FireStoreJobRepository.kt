package com.example.valentinesgarage.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.example.valentinesgarage.data.mapper.toDomain
import com.example.valentinesgarage.data.mapper.toDto
import com.example.valentinesgarage.data.model.RepairJobDto
import com.example.valentinesgarage.domain.model.RepairJob
import com.example.valentinesgarage.domain.model.JobStatus
import com.example.valentinesgarage.domain.repository.JobRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirestoreJobRepository(private val firestore: FirebaseFirestore) : JobRepository {

    override fun getActiveJobsFlow(): Flow<List<RepairJob>> = callbackFlow {
        val subscription = firestore.collection("repairJobs")
            .whereNotEqualTo("status", "COMPLETED")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val jobs = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(RepairJobDto::class.java)?.copy(jobId = doc.id)?.toDomain()
                } ?: emptyList()
                trySend(jobs)
            }
        awaitClose { subscription.remove() }
    }

    override suspend fun getJobById(jobId: String): Result<RepairJob> {
        return try {
            val doc = firestore.collection("repairJobs").document(jobId).get().await()
            val dto = doc.toObject(RepairJobDto::class.java)?.copy(jobId = doc.id) ?: throw Exception("Job not found")
            Result.success(dto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateJob(job: RepairJob): Result<Unit> {
        return try {
            firestore.collection("repairJobs")
                .document(job.jobId)
                .set(job.toDto())
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createJobForTruck(truckId: String): Result<String> {
        try {
            val job = RepairJobDto(truckId = truckId, status = JobStatus.CHECKED_IN.name)
            val docRef = firestore.collection("repairJobs").document()
            job.jobId = docRef.id
            docRef.set(job).await()
            return Result.success(docRef.id)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}