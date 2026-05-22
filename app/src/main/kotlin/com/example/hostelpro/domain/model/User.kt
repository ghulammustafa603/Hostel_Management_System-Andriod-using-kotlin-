package com.example.hostelpro.domain.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val role: String,
    val profilePhotoUrl: String? = null
)
