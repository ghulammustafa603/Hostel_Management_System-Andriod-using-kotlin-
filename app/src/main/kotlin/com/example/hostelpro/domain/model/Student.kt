package com.example.hostelpro.domain.model

data class Student(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val guardianName: String,
    val guardianPhone: String,
    val roomId: String?,
    val joiningDate: Long,
    val checkOutDate: Long?,
    val feeStatus: String,
    val photoUrl: String?,
    val status: String
)
