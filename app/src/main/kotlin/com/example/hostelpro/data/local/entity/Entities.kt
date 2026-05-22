package com.example.hostelpro.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rooms")
data class RoomEntity(
    @PrimaryKey
    val id: String,
    val roomNumber: String,
    val floor: Int,
    val type: String,  // SINGLE, DOUBLE, TRIPLE, DORM
    val capacity: Int,
    val occupiedCount: Int,
    val monthlyRent: Double,
    val amenities: String,  // JSON list
    val status: String,  // AVAILABLE, OCCUPIED, MAINTENANCE
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "students")
data class StudentEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val guardianName: String,
    val guardianPhone: String,
    val roomId: String?,
    val joiningDate: Long,
    val checkOutDate: Long?,
    val feeStatus: String,  // PAID, PARTIAL, DUE, OVERDUE
    val photoUrl: String?,
    val status: String,  // ACTIVE, CHECKED_OUT, SUSPENDED
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "fees")
data class FeeEntity(
    @PrimaryKey
    val id: String,
    val studentId: String,
    val month: String,  // "2025-06"
    val rentAmount: Double,
    val messAmount: Double,
    val otherCharges: Double,
    val totalAmount: Double,
    val paidAmount: Double,
    val paymentDate: Long?,
    val paymentMode: String?,  // CASH, UPI, BANK_TRANSFER, CHEQUE
    val status: String,  // PAID, PARTIAL, DUE, OVERDUE
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "complaints")
data class ComplaintEntity(
    @PrimaryKey
    val id: String,
    val studentId: String,
    val roomId: String,
    val category: String,
    val description: String,
    val photoUrl: String?,
    val status: String,  // OPEN, IN_PROGRESS, RESOLVED
    val createdAt: Long = System.currentTimeMillis(),
    val resolvedAt: Long?,
    val assignedStaffId: String?,
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "visitors")
data class VisitorEntity(
    @PrimaryKey
    val id: String,
    val visitorName: String,
    val visitorPhone: String,
    val studentId: String,
    val purpose: String,
    val inTime: Long,
    val outTime: Long?,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val role: String,  // ADMIN, STAFF, STUDENT
    val profilePhotoUrl: String?,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
