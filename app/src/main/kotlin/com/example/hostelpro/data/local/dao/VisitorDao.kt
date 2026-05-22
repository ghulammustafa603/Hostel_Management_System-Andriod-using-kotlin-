package com.example.hostelpro.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.hostelpro.data.local.entity.VisitorEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VisitorDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisitor(visitor: VisitorEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisitors(visitors: List<VisitorEntity>)
    
    @Update
    suspend fun updateVisitor(visitor: VisitorEntity)
    
    @Delete
    suspend fun deleteVisitor(visitor: VisitorEntity)
    
    @Query("SELECT * FROM visitors WHERE id = :visitorId")
    fun getVisitorById(visitorId: String): Flow<VisitorEntity?>
    
    @Query("SELECT * FROM visitors ORDER BY inTime DESC")
    fun getAllVisitors(): Flow<List<VisitorEntity>>
    
    @Query("SELECT * FROM visitors WHERE studentId = :studentId ORDER BY inTime DESC")
    fun getVisitorsByStudent(studentId: String): Flow<List<VisitorEntity>>
    
    @Query("SELECT * FROM visitors WHERE outTime IS NULL ORDER BY inTime DESC")
    fun getActiveVisitors(): Flow<List<VisitorEntity>>
}
