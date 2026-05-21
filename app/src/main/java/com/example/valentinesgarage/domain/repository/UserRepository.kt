package com.example.valentinesgarage.domain.repository

import com.example.valentinesgarage.domain.model.User

// Data class representing a user in the system
data class User(
    val id: String,        // Firebase Auth UID
    val name: String,
    val email: String,
    val role: String       // "admin" or "mechanic"
)

// Repository interface for user-related operations
interface UserRepository {
    /**
     * Get the role of a user by their UID.
     * @param uid Firebase Authentication user ID
     * @return The role as a string (e.g., "admin", "mechanic") or null if not found
     */
    suspend fun getUserRole(uid: String): String?

    /**
     * Get full user details by UID.
     * @param uid Firebase Authentication user ID
     * @return User object or null if not found
     */
    suspend fun getUser(uid: String): User?

    /**
     * Get all users (admin only).
     * @return List of all users
     */
    suspend fun getAllUsers(): List<User>
}