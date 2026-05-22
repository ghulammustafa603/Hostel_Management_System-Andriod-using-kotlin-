package com.example.hostelpro.utils

object Constants {
    // User Roles
    const val ROLE_ADMIN = "ADMIN"
    const val ROLE_STAFF = "STAFF"
    const val ROLE_STUDENT = "STUDENT"

    // Room Types
    const val ROOM_TYPE_SINGLE = "SINGLE"
    const val ROOM_TYPE_DOUBLE = "DOUBLE"
    const val ROOM_TYPE_TRIPLE = "TRIPLE"
    const val ROOM_TYPE_DORM = "DORM"

    // Room Status
    const val ROOM_STATUS_AVAILABLE = "AVAILABLE"
    const val ROOM_STATUS_OCCUPIED = "OCCUPIED"
    const val ROOM_STATUS_MAINTENANCE = "MAINTENANCE"

    // Student Status
    const val STUDENT_STATUS_ACTIVE = "ACTIVE"
    const val STUDENT_STATUS_CHECKED_OUT = "CHECKED_OUT"
    const val STUDENT_STATUS_SUSPENDED = "SUSPENDED"

    // Fee Status
    const val FEE_STATUS_PAID = "PAID"
    const val FEE_STATUS_PARTIAL = "PARTIAL"
    const val FEE_STATUS_DUE = "DUE"
    const val FEE_STATUS_OVERDUE = "OVERDUE"

    // Payment Mode
    const val PAYMENT_MODE_CASH = "CASH"
    const val PAYMENT_MODE_UPI = "UPI"
    const val PAYMENT_MODE_BANK_TRANSFER = "BANK_TRANSFER"
    const val PAYMENT_MODE_CHEQUE = "CHEQUE"

    // Complaint Status
    const val COMPLAINT_STATUS_OPEN = "OPEN"
    const val COMPLAINT_STATUS_IN_PROGRESS = "IN_PROGRESS"
    const val COMPLAINT_STATUS_RESOLVED = "RESOLVED"
}

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val exception: Throwable) : UiState<Nothing>()
}

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
    object Loading : Result<Nothing>()
}
