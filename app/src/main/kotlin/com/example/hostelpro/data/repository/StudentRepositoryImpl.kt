package com.example.hostelpro.data.repository

import com.example.hostelpro.data.local.dao.StudentDao
import com.example.hostelpro.data.local.entity.StudentEntity
import com.example.hostelpro.domain.model.Student
import com.example.hostelpro.domain.repository.StudentRepository
import com.example.hostelpro.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StudentRepositoryImpl @Inject constructor(
    private val studentDao: StudentDao
) : StudentRepository {

    override fun getAllStudents(): Flow<Result<List<Student>>> =
        studentDao.getAllStudents()
            .map<List<StudentEntity>, Result<List<Student>>> { entities ->
                Result.Success(entities.map { it.toDomain() })
            }
            .catch { e -> emit(Result.Error(e)) }

    override suspend fun addStudent(student: Student): Result<String> {
        return try {
            studentDao.insertStudent(student.toEntity())
            Result.Success(student.id)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun deleteStudent(studentId: String): Result<Unit> {
        return try {
            val entity = studentDao.getStudentById(studentId).first()
            if (entity != null) studentDao.deleteStudent(entity)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override fun getActiveStudentCount(): Flow<Result<Int>> =
        studentDao.getActiveStudentCount()
            .map<Int, Result<Int>> { Result.Success(it) }
            .catch { e -> emit(Result.Error(e)) }

    private fun StudentEntity.toDomain() = Student(
        id = id, name = name, email = email, phone = phone,
        guardianName = guardianName, guardianPhone = guardianPhone,
        roomId = roomId, joiningDate = joiningDate, checkOutDate = checkOutDate,
        feeStatus = feeStatus, photoUrl = photoUrl, status = status
    )

    private fun Student.toEntity() = StudentEntity(
        id = id, name = name, email = email, phone = phone,
        guardianName = guardianName, guardianPhone = guardianPhone,
        roomId = roomId, joiningDate = joiningDate, checkOutDate = checkOutDate,
        feeStatus = feeStatus, photoUrl = photoUrl, status = status
    )
}
