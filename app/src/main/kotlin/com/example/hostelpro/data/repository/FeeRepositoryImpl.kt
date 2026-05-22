package com.example.hostelpro.data.repository

import com.example.hostelpro.data.local.dao.FeeDao
import com.example.hostelpro.data.local.entity.FeeEntity
import com.example.hostelpro.domain.model.Fee
import com.example.hostelpro.domain.repository.FeeRepository
import com.example.hostelpro.utils.Constants
import com.example.hostelpro.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FeeRepositoryImpl @Inject constructor(
    private val feeDao: FeeDao
) : FeeRepository {

    override fun getAllFees(): Flow<Result<List<Fee>>> =
        feeDao.getAllFees()
            .map<List<FeeEntity>, Result<List<Fee>>> { entities ->
                Result.Success(entities.map { it.toDomain() })
            }
            .catch { e -> emit(Result.Error(e)) }

    override suspend fun addFee(fee: Fee): Result<String> {
        return try {
            feeDao.insertFee(fee.toEntity())
            Result.Success(fee.id)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun recordPayment(feeId: String, amount: Double, mode: String): Result<Unit> {
        return try {
            val entity = feeDao.getFeeById(feeId).first() ?: return Result.Error(Exception("Fee not found"))
            val newPaid = entity.paidAmount + amount
            val status = when {
                newPaid >= entity.totalAmount -> Constants.FEE_STATUS_PAID
                newPaid > 0 -> Constants.FEE_STATUS_PARTIAL
                else -> Constants.FEE_STATUS_DUE
            }
            feeDao.updateFee(
                entity.copy(
                    paidAmount = newPaid,
                    paymentDate = System.currentTimeMillis(),
                    paymentMode = mode,
                    status = status,
                    updatedAt = System.currentTimeMillis()
                )
            )
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override fun getPendingAmount(): Flow<Result<Double>> =
        feeDao.getTotalPendingAmount()
            .map<Double?, Result<Double>> { Result.Success(it ?: 0.0) }
            .catch { e -> emit(Result.Error(e)) }

    private fun FeeEntity.toDomain() = Fee(
        id = id, studentId = studentId, month = month,
        rentAmount = rentAmount, messAmount = messAmount, otherCharges = otherCharges,
        totalAmount = totalAmount, paidAmount = paidAmount,
        paymentDate = paymentDate, paymentMode = paymentMode, status = status
    )

    private fun Fee.toEntity() = FeeEntity(
        id = id, studentId = studentId, month = month,
        rentAmount = rentAmount, messAmount = messAmount, otherCharges = otherCharges,
        totalAmount = totalAmount, paidAmount = paidAmount,
        paymentDate = paymentDate, paymentMode = paymentMode, status = status
    )
}
