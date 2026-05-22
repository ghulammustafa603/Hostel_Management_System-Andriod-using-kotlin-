package com.example.hostelpro.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.hostelpro.data.local.entity.StudentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: StudentEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudents(students: List<StudentEntity>)
    
    @Update
    suspend fun updateStudent(student: StudentEntity)
    
    @Delete
    suspend fun deleteStudent(student: StudentEntity)
    
    @Query("SELECT * FROM students WHERE id = :studentId")
    fun getStudentById(studentId: String): Flow<StudentEntity?>
    
    @Query("SELECT * FROM students ORDER BY name ASC")
    fun getAllStudents(): Flow<List<StudentEntity>>
    
    @Query("SELECT * FROM students WHERE status = :status ORDER BY name ASC")
    fun getStudentsByStatus(status: String): Flow<List<StudentEntity>>
    
    @Query("SELECT * FROM students WHERE roomId = :roomId ORDER BY name ASC")
    fun getStudentsByRoom(roomId: String): Flow<List<StudentEntity>>
    
    @Query("SELECT * FROM students WHERE feeStatus = :feeStatus ORDER BY name ASC")
    fun getStudentsByFeeStatus(feeStatus: String): Flow<List<StudentEntity>>
    
    @Query("SELECT COUNT(*) FROM students WHERE status = 'ACTIVE'")
    fun getActiveStudentCount(): Flow<Int>
    
    @Query("SELECT COUNT(*) FROM students")
    fun getTotalStudentCount(): Flow<Int>
}
