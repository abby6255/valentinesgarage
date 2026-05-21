package com.example.valentinesgarage.domain.model

data class Truck(
    val id: String = "",
    val licensePlate: String,
    val model: String = "",
    val odometerKm: Int,
    val conditionNotes: String,
    val checkInTimestamp: Long = System.currentTimeMillis()
)