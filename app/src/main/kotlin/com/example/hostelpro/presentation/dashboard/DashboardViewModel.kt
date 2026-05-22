package com.example.hostelpro.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hostelpro.domain.repository.AuthRepository
import com.example.hostelpro.domain.repository.ComplaintRepository
import com.example.hostelpro.domain.repository.FeeRepository
import com.example.hostelpro.domain.repository.RoomRepository
import com.example.hostelpro.domain.repository.StudentRepository
import com.example.hostelpro.domain.usecase.room.GetRoomStatsUseCase
import com.example.hostelpro.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardUiState(
    val userName: String = "",
    val userRole: String = "",
    val totalRooms: Int = 0,
    val occupiedRooms: Int = 0,
    val availableRooms: Int = 0,
    val totalStudents: Int = 0,
    val pendingFees: Double = 0.0,
    val openComplaints: Int = 0
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val getRoomStatsUseCase: GetRoomStatsUseCase,
    private val studentRepository: StudentRepository,
    private val feeRepository: FeeRepository,
    private val complaintRepository: ComplaintRepository,
    private val roomRepository: RoomRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        val user = authRepository.getCurrentUser()
        _uiState.value = _uiState.value.copy(
            userName = user?.name ?: "User",
            userRole = user?.role ?: ""
        )
        loadStats()
        viewModelScope.launch { roomRepository.syncRoomsWithCloud() }
    }

    private fun loadStats() {
        viewModelScope.launch {
            getRoomStatsUseCase().collect { result ->
                if (result is Result.Success) {
                    _uiState.value = _uiState.value.copy(
                        totalRooms = result.data.totalRooms,
                        occupiedRooms = result.data.occupiedRooms,
                        availableRooms = result.data.availableRooms
                    )
                }
            }
        }
        viewModelScope.launch {
            studentRepository.getActiveStudentCount().collect { result ->
                if (result is Result.Success) {
                    _uiState.value = _uiState.value.copy(totalStudents = result.data)
                }
            }
        }
        viewModelScope.launch {
            feeRepository.getPendingAmount().collect { result ->
                if (result is Result.Success) {
                    _uiState.value = _uiState.value.copy(pendingFees = result.data)
                }
            }
        }
        viewModelScope.launch {
            complaintRepository.getOpenCount().collect { result ->
                if (result is Result.Success) {
                    _uiState.value = _uiState.value.copy(openComplaints = result.data)
                }
            }
        }
    }

    fun logout() = authRepository.logout()
}
