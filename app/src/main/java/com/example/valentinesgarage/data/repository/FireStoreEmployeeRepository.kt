package com.example.valentinesgarage.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.example.valentinesgarage.data.model.EmployeeDto
import com.example.valentinesgarage.data.model.RepairJobDto
import com.example.valentinesgarage.data.model.toDomain
import com.example.valentinesgarage.domain.model.*
import com.example.valentinesgarage.domain.repository.EmployeeRepository
import kotlinx.coroutines.tasks.await
import com.example.valentinesgarage.domain.model.EmployeeReport

// Assume EmployeeDto is a simple data class with id, name, role
// and a toDomain() extension.

class FirestoreEmployeeRepository(private val firestore: FirebaseFirestore) : EmployeeRepository {

    override suspend fun getAllEmployees(): List<Employee> {
        return try {
            val snapshot = firestore.collection("employees").get().await()
            snapshot.documents.mapNotNull { doc ->
                val name = doc.getString("name") ?: return@mapNotNull null
                val role = doc.getString("role") ?: "mechanic"
                Employee(id = doc.id, name = name, role = role)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
    override suspend fun getReportForEmployee(employeeId: String): EmployeeReport {
        // 1. Get employee doc
        val empDoc = firestore.collection("employees").document(employeeId).get().await()
        val employee = empDoc.toObject(EmployeeDto::class.java)?.toDomain(employeeId)
            ?: throw Exception("Employee not found")

        // 2. Query all jobs where tasks.completedByEmployeeId == employeeId
        // For simplicity, we query all non-completed jobs (or all) and filter in code.
        // In production, use a collection group index.
        val allJobsSnapshot = firestore.collection("repairJobs").get().await()
        val completedTasks = mutableListOf<CompletedTaskDetail>()
        val servicedTruckSet = mutableSetOf<String>()
        var odometerSum = 0.0
        var odometerCount = 0

        for (doc in allJobsSnapshot.documents) {
            val job = doc.toObject(RepairJobDto::class.java) ?: continue
            job.tasks.forEach { task ->
                if (task.completedByEmployeeId == employeeId && task.completed) {
                    // Fetch truck license plate
                    val truckDoc = firestore.collection("trucks").document(job.truckId).get().await()
                    val plate = truckDoc.getString("licensePlate") ?: "Unknown"
                    completedTasks.add(
                        CompletedTaskDetail(
                            truckLicensePlate = plate,
                            taskDescription = task.description,
                            completedAt = System.currentTimeMillis(), // ideally store timestamp in task
                            notes = task.notes
                        )
                    )
                    servicedTruckSet.add(job.truckId)
                }
            }
            // Also collect odometer for all trucks this employee touched
            val truckDoc = firestore.collection("trucks").document(job.truckId).get().await()
            val odometer = truckDoc.getLong("odometerKm")?.toInt() ?: continue
            odometerSum += odometer
            odometerCount++
        }

        val avgOdometer = if (odometerCount > 0) odometerSum / odometerCount else 0.0
        return EmployeeReport(
            employee = employee,
            completedTasks = completedTasks,
            servicedTruckCount = servicedTruckSet.size,
            averageOdometerAtCheckIn = avgOdometer
        )
    }
}