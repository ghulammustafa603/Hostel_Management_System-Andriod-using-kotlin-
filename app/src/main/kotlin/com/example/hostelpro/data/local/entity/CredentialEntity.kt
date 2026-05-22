package com.example.hostelpro.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "credentials")
data class CredentialEntity(
    @PrimaryKey
    val email: String,
    val password: String,
    val userId: String
)
