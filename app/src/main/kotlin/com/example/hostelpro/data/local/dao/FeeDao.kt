package com.example.hostelpro.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.hostelpro.data.local.entity.FeeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FeeDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFee(fee: FeeEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFees(fees: List<FeeEntity>)
    
    @Update
    suspend fun updateFee(fee: FeeEntity)
    
    @Delete
    suspend fun deleteFee(fee: FeeEntity)
    
    @Query("SELECT * FROM fees ORDER BY month DESC")
    fun getAllFees(): Flow<List<FeeEntity>>

    @Query("SELECT * FROM fees WHERE id = :feeId")
    fun getFeeById(feeId: String): Flow<FeeEntity?>
    
    @Query("SELECT * FROM fees WHERE studentId = :studentId ORDER BY month DESC")
    fun getFeesByStudent(studentId: String): Flow<List<FeeEntity>>
    
    @Query("SELECT * FROM fees WHERE month = :month ORDER BY studentId ASC")
    fun getFeesByMonth(month: String): Flow<List<FeeEntity>>
    
    @Query("SELECT * FROM fees WHERE status = :status ORDER BY month DESC")
    fun getFeesByStatus(status: String): Flow<List<FeeEntity>>
    
    @Query("SELECT SUM(totalAmount - paidAmount) FROM fees WHERE status IN ('DUE', 'OVERDUE', 'PARTIAL')")
    fun getTotalPendingAmount(): Flow<Double>
    
    @Query("SELECT SUM(paidAmount) FROM fees WHERE paymentDate IS NOT NULL")
    fun getTotalCollectedAmount(): Flow<Double>
}
