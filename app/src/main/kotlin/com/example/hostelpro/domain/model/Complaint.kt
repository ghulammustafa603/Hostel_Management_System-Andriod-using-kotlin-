package com.example.hostelpro.domain.model

data class Complaint(
    val id: String,
    val studentId: String,
    val roomId: String,
    val category: String,
    val description: String,
    val photoUrl: String?,
    val status: String,
    val createdAt: Long,
    val resolvedAt: Long?,
    val assignedStaffId: String?
)
