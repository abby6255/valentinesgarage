package com.example.valentinesgarage.data.model


data class TruckDto(
    var id: String = "",
    val licensePlate: String = "",
    val model: String = "",
    val odometerKm: Int = 0,
    val conditionNotes: String = "",
    val checkInTimestamp: Long = 0L
)