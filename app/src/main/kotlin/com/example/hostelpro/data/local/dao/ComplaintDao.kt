package com.example.hostelpro.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.hostelpro.data.local.entity.ComplaintEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ComplaintDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComplaint(complaint: ComplaintEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComplaints(complaints: List<ComplaintEntity>)
    
    @Update
    suspend fun updateComplaint(complaint: ComplaintEntity)
    
    @Delete
    suspend fun deleteComplaint(complaint: ComplaintEntity)
    
    @Query("SELECT * FROM complaints WHERE id = :complaintId")
    fun getComplaintById(complaintId: String): Flow<ComplaintEntity?>
    
    @Query("SELECT * FROM complaints ORDER BY createdAt DESC")
    fun getAllComplaints(): Flow<List<ComplaintEntity>>
    
    @Query("SELECT * FROM complaints WHERE studentId = :studentId ORDER BY createdAt DESC")
    fun getComplaintsByStudent(studentId: String): Flow<List<ComplaintEntity>>
    
    @Query("SELECT * FROM complaints WHERE status = :status ORDER BY createdAt DESC")
    fun getComplaintsByStatus(status: String): Flow<List<ComplaintEntity>>
    
    @Query("SELECT * FROM complaints WHERE category = :category ORDER BY createdAt DESC")
    fun getComplaintsByCategory(category: String): Flow<List<ComplaintEntity>>
    
    @Query("SELECT COUNT(*) FROM complaints WHERE status = 'OPEN'")
    fun getOpenComplaintCount(): Flow<Int>
}
