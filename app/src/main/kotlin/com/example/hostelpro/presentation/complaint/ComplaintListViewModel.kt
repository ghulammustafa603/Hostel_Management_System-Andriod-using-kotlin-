package com.example.hostelpro.presentation.complaint

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hostelpro.data.local.SessionManager
import com.example.hostelpro.domain.model.Complaint
import com.example.hostelpro.domain.repository.ComplaintRepository
import com.example.hostelpro.utils.Constants
import com.example.hostelpro.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class ComplaintListUiState(
    val complaints: List<Complaint> = emptyList(),
    val isLoading: Boolean = true,
    val showAddDialog: Boolean = false,
    val category: String = "Maintenance",
    val description: String = ""
)

@HiltViewModel
class ComplaintListViewModel @Inject constructor(
    private val complaintRepository: ComplaintRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ComplaintListUiState())
    val uiState: StateFlow<ComplaintListUiState> = _uiState.asStateFlow()

    init { loadComplaints() }

    private fun loadComplaints() {
        viewModelScope.launch {
            val flow = if (sessionManager.getRole() == Constants.ROLE_STUDENT) {
                complaintRepository.getComplaintsForStudent(sessionManager.getUserId() ?: "student-1")
            } else {
                complaintRepository.getAllComplaints()
            }
            flow.collect { result ->
                if (result is Result.Success) {
                    _uiState.value = _uiState.value.copy(complaints = result.data, isLoading = false)
                }
            }
        }
    }

    fun showAddDialog() { _uiState.value = _uiState.value.copy(showAddDialog = true) }
    fun hideAddDialog() { _uiState.value = _uiState.value.copy(showAddDialog = false) }
    fun onCategoryChanged(v: String) { _uiState.value = _uiState.value.copy(category = v) }
    fun onDescriptionChanged(v: String) { _uiState.value = _uiState.value.copy(description = v) }

    fun addComplaint() {
        val s = _uiState.value
        if (s.description.isBlank()) return
        viewModelScope.launch {
            val studentId = sessionManager.getUserId() ?: "student-1"
            complaintRepository.addComplaint(
                Complaint(
                    id = UUID.randomUUID().toString(),
                    studentId = studentId,
                    roomId = "room-101",
                    category = s.category,
                    description = s.description,
                    photoUrl = null,
                    status = Constants.COMPLAINT_STATUS_OPEN,
                    createdAt = System.currentTimeMillis(),
                    resolvedAt = null,
                    assignedStaffId = null
                )
            )
            _uiState.value = _uiState.value.copy(showAddDialog = false, description = "")
        }
    }

    fun resolveComplaint(id: String) {
        viewModelScope.launch {
            complaintRepository.updateStatus(id, Constants.COMPLAINT_STATUS_RESOLVED)
        }
    }
}
