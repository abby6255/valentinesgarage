package com.example.valentinesgarage.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.example.valentinesgarage.data.model.UserDto
import com.example.valentinesgarage.data.model.toDomain
import com.example.valentinesgarage.domain.model.User
import com.example.valentinesgarage.domain.repository.UserRepository
import kotlinx.coroutines.tasks.await

class FirestoreUserRepository(
    private val firestore: FirebaseFirestore
) : UserRepository {

    override suspend fun getUserRole(uid: String): String? {
        return try {
            val doc = firestore.collection("users").document(uid).get().await()
            doc.getString("role")
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getUser(uid: String): User? {
        return try {
            val doc = firestore.collection("users").document(uid).get().await()
            val dto = doc.toObject(UserDto::class.java) ?: return null
            dto.toDomain(uid)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getAllUsers(): List<User> {
        return try {
            val snapshot = firestore.collection("users").get().await()
            snapshot.documents.mapNotNull { doc ->
                val dto = doc.toObject(UserDto::class.java) ?: return@mapNotNull null
                dto.toDomain(doc.id)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}