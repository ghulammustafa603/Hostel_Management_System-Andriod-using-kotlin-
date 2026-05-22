package com.example.hostelpro.domain.repository

import com.example.hostelpro.domain.model.Complaint
import com.example.hostelpro.utils.Result
import kotlinx.coroutines.flow.Flow

interface ComplaintRepository {
    fun getAllComplaints(): Flow<Result<List<Complaint>>>
    fun getComplaintsForStudent(studentId: String): Flow<Result<List<Complaint>>>
    suspend fun addComplaint(complaint: Complaint): Result<String>
    suspend fun updateStatus(complaintId: String, status: String): Result<Unit>
    fun getOpenCount(): Flow<Result<Int>>
}
