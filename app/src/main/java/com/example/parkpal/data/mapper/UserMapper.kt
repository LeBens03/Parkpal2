package com.example.parkpal.data.mapper

import com.example.parkpal.data.entity.UserEntity
import com.example.parkpal.domain.model.User

fun UserEntity.toUser(): User {
    return User(
        userId = userId,
        name = name,
        email = email,
        password = password,
        phoneNumber = phoneNumber,
        city = city,
        birthDate = birthDate,
        address = address,
        country = country,
        zipCode = zipCode,
        gender = gender
    )
}

fun User.toUserEntity(): UserEntity {
    return UserEntity(
        userId = userId,
        name = name,
        email = email,
        password = password,
        phoneNumber = phoneNumber,
        city = city,
        birthDate = birthDate,
        address = address,
        country = country,
        zipCode = zipCode,
        gender = gender
    )
}