package com.example.hostelpro.domain.model

data class Fee(
    val id: String,
    val studentId: String,
    val month: String,
    val rentAmount: Double,
    val messAmount: Double,
    val otherCharges: Double,
    val totalAmount: Double,
    val paidAmount: Double,
    val paymentDate: Long?,
    val paymentMode: String?,
    val status: String
) {
    val dueAmount: Double get() = (totalAmount - paidAmount).coerceAtLeast(0.0)
}
