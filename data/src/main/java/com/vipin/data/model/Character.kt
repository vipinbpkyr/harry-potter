package com.vipin.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "characters")
data class Character(
    @PrimaryKey
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("actor")
    val actor: String,
    @SerializedName("species")
    val species: String,
    @SerializedName("house")
    val house: String?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("dateOfBirth")
    val dateOfBirth: String?,
    @SerializedName("alive")
    val alive: Boolean
)