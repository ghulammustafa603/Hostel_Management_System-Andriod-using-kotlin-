package com.example.hostelpro.domain.repository

import com.example.hostelpro.domain.model.Student
import com.example.hostelpro.utils.Result
import kotlinx.coroutines.flow.Flow

interface StudentRepository {
    fun getAllStudents(): Flow<Result<List<Student>>>
    suspend fun addStudent(student: Student): Result<String>
    suspend fun deleteStudent(studentId: String): Result<Unit>
    fun getActiveStudentCount(): Flow<Result<Int>>
}
