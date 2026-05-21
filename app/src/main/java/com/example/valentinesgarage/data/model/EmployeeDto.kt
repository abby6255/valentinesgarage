package com.example.valentinesgarage.data.model

import com.example.valentinesgarage.domain.model.Employee


data class EmployeeDto(
    val name: String = "",
    val role: String = ""
)

fun EmployeeDto.toDomain(id: String) = Employee(id, name, role)