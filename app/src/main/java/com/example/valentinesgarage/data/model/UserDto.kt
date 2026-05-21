package com.example.valentinesgarage.data.model

import com.example.valentinesgarage.domain.model.User

data class UserDto(
    val name: String = "",
    val email: String = "",
    val role: String = "mechanic"   // default role
)

fun UserDto.toDomain(id: String): User = User(
    id = id,
    name = name,
    email = email,
    role = role
)