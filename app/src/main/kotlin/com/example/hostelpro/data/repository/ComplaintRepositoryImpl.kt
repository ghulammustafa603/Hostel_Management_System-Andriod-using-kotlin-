package com.example.hostelpro.data.repository

import com.example.hostelpro.data.local.dao.ComplaintDao
import com.example.hostelpro.data.local.entity.ComplaintEntity
import com.example.hostelpro.domain.model.Complaint
import com.example.hostelpro.domain.repository.ComplaintRepository
import com.example.hostelpro.utils.Constants
import com.example.hostelpro.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ComplaintRepositoryImpl @Inject constructor(
    private val complaintDao: ComplaintDao
) : ComplaintRepository {

    override fun getAllComplaints(): Flow<Result<List<Complaint>>> =
        complaintDao.getAllComplaints()
            .map<List<ComplaintEntity>, Result<List<Complaint>>> { entities ->
                Result.Success(entities.map { it.toDomain() })
            }
            .catch { e -> emit(Result.Error(e)) }

    override fun getComplaintsForStudent(studentId: String): Flow<Result<List<Complaint>>> =
        complaintDao.getComplaintsByStudent(studentId)
            .map<List<ComplaintEntity>, Result<List<Complaint>>> { entities ->
                Result.Success(entities.map { it.toDomain() })
            }
            .catch { e -> emit(Result.Error(e)) }

    override suspend fun addComplaint(complaint: Complaint): Result<String> {
        return try {
            complaintDao.insertComplaint(complaint.toEntity())
            Result.Success(complaint.id)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateStatus(complaintId: String, status: String): Result<Unit> {
        return try {
            val entity = complaintDao.getComplaintById(complaintId).first()
                ?: return Result.Error(Exception("Complaint not found"))
            complaintDao.updateComplaint(
                entity.copy(
                    status = status,
                    resolvedAt = if (status == Constants.COMPLAINT_STATUS_RESOLVED) System.currentTimeMillis() else null,
                    updatedAt = System.currentTimeMillis()
                )
            )
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override fun getOpenCount(): Flow<Result<Int>> =
        complaintDao.getOpenComplaintCount()
            .map<Int, Result<Int>> { Result.Success(it) }
            .catch { e -> emit(Result.Error(e)) }

    private fun ComplaintEntity.toDomain() = Complaint(
        id = id, studentId = studentId, roomId = roomId, category = category,
        description = description, photoUrl = photoUrl, status = status,
        createdAt = createdAt, resolvedAt = resolvedAt, assignedStaffId = assignedStaffId
    )

    private fun Complaint.toEntity() = ComplaintEntity(
        id = id, studentId = studentId, roomId = roomId, category = category,
        description = description, photoUrl = photoUrl, status = status,
        createdAt = createdAt, resolvedAt = resolvedAt, assignedStaffId = assignedStaffId
    )
}
