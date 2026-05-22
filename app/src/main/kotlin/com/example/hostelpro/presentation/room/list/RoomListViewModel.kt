package com.example.hostelpro.presentation.room.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hostelpro.domain.model.Room
import com.example.hostelpro.domain.usecase.room.GetAllRoomsUseCase
import com.example.hostelpro.domain.usecase.room.GetRoomStatsUseCase
import com.example.hostelpro.domain.usecase.room.RoomStats
import com.example.hostelpro.utils.Result
import com.example.hostelpro.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RoomListUiState(
    val rooms: List<Room> = emptyList(),
    val stats: RoomStats? = null,
    val uiState: UiState<Unit> = UiState.Loading,
    val selectedFilter: String = "ALL",  // ALL, AVAILABLE, OCCUPIED, MAINTENANCE
    val searchQuery: String = "",
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

@HiltViewModel
class RoomListViewModel @Inject constructor(
    private val getAllRoomsUseCase: GetAllRoomsUseCase,
    private val getRoomStatsUseCase: GetRoomStatsUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(RoomListUiState())
    val uiState: StateFlow<RoomListUiState> = _uiState.asStateFlow()

    init {
        loadRooms()
        loadRoomStats()
    }

    private fun loadRooms() {
        viewModelScope.launch {
            getAllRoomsUseCase()
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        uiState = UiState.Error(e),
                        errorMessage = e.message ?: "Failed to load rooms",
                        isLoading = false
                    )
                }
                .collect { result ->
                    when (result) {
                        is Result.Success -> {
                            val filteredRooms = filterRooms(result.data)
                            _uiState.value = _uiState.value.copy(
                                rooms = filteredRooms,
                                uiState = UiState.Success(Unit),
                                isLoading = false,
                                errorMessage = null
                            )
                        }
                        is Result.Error -> {
                            _uiState.value = _uiState.value.copy(
                                uiState = UiState.Error(result.exception),
                                errorMessage = result.exception.message ?: "Failed to load rooms",
                                isLoading = false
                            )
                        }
                        else -> {}
                    }
                }
        }
    }

    private fun loadRoomStats() {
        viewModelScope.launch {
            getRoomStatsUseCase()
                .catch { e ->
                    // Stats loading failed, but don't show error to user
                }
                .collect { result ->
                    if (result is Result.Success) {
                        _uiState.value = _uiState.value.copy(stats = result.data)
                    }
                }
        }
    }

    fun onFilterChanged(filter: String) {
        _uiState.value = _uiState.value.copy(selectedFilter = filter)
        applyFilters()
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        applyFilters()
    }

    private fun applyFilters() {
        loadRooms()
    }

    private fun filterRooms(rooms: List<Room>): List<Room> {
        val currentState = _uiState.value
        
        return rooms.filter { room ->
            val statusMatch = when (currentState.selectedFilter) {
                "ALL" -> true
                "AVAILABLE" -> room.status == "AVAILABLE"
                "OCCUPIED" -> room.status == "OCCUPIED"
                "MAINTENANCE" -> room.status == "MAINTENANCE"
                else -> true
            }
            
            val searchMatch = if (currentState.searchQuery.isNotEmpty()) {
                room.roomNumber.contains(currentState.searchQuery, ignoreCase = true) ||
                room.type.contains(currentState.searchQuery, ignoreCase = true)
            } else {
                true
            }
            
            statusMatch && searchMatch
        }.sortedBy { it.roomNumber }
    }

    fun onRetryClicked() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        loadRooms()
        loadRoomStats()
    }
}
