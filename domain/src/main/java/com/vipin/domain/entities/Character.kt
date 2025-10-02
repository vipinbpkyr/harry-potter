package com.vipin.domain.entities

data class Character(
    val id: String,
    val name: String,
    val actor: String,
    val species: String,
    val house: String?,
    val image: String?,
    val dateOfBirth: String?,
    val alive: Boolean
)