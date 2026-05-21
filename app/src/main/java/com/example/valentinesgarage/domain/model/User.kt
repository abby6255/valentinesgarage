package com.example.valentinesgarage.domain.model

data class User(
    val id: String,      // Firebase Authentication UID (matches Firestore document ID)
    val name: String,
    val email: String,
    val role: String     // "admin" or "mechanic"
)