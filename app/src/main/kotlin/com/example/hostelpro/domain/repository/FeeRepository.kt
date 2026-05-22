package com.example.hostelpro.domain.repository

import com.example.hostelpro.domain.model.Fee
import com.example.hostelpro.utils.Result
import kotlinx.coroutines.flow.Flow

interface FeeRepository {
    fun getAllFees(): Flow<Result<List<Fee>>>
    suspend fun addFee(fee: Fee): Result<String>
    suspend fun recordPayment(feeId: String, amount: Double, mode: String): Result<Unit>
    fun getPendingAmount(): Flow<Result<Double>>
}
