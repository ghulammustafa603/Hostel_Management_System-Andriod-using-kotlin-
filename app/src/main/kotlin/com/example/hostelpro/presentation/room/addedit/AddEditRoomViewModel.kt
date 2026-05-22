package com.example.hostelpro.presentation.room.addedit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hostelpro.domain.model.Room
import com.example.hostelpro.domain.model.RoomStatus
import com.example.hostelpro.domain.model.RoomType
import com.example.hostelpro.domain.usecase.room.AddRoomUseCase
import com.example.hostelpro.domain.usecase.room.GetRoomByIdUseCase
import com.example.hostelpro.domain.usecase.room.UpdateRoomUseCase
import com.example.hostelpro.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.util.UUID

data class AddEditRoomUiState(
    val roomId: String? = null,
    val roomNumber: String = "",
    val floor: Int = 1,
    val type: String = RoomType.SINGLE.name,
    val capacity: Int = 1,
    val occupiedCount: Int = 0,
    val monthlyRent: Double = 0.0,
    val amenities: List<String> = emptyList(),
    val status: String = RoomStatus.AVAILABLE.name,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val errorMessage: String? = null,
    val roomNumberError: String? = null,
    val rentError: String? = null,
    val capacityError: String? = null
)

@HiltViewModel
class AddEditRoomViewModel @Inject constructor(
    private val addRoomUseCase: AddRoomUseCase,
    private val updateRoomUseCase: UpdateRoomUseCase,
    private val getRoomByIdUseCase: GetRoomByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    private val roomId: String? = savedStateHandle.get<String>("roomId")
    
    private val _uiState = MutableStateFlow(AddEditRoomUiState(roomId = roomId))
    val uiState: StateFlow<AddEditRoomUiState> = _uiState.asStateFlow()

    init {
        if (roomId != null) {
            loadRoom(roomId)
        }
    }

    private fun loadRoom(roomId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            getRoomByIdUseCase(roomId)
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to load room"
                    )
                }
                .collect { result ->
                    if (result is Result.Success && result.data != null) {
                        val room = result.data
                        _uiState.value = _uiState.value.copy(
                            roomNumber = room.roomNumber,
                            floor = room.floor,
                            type = room.type,
                            capacity = room.capacity,
                            occupiedCount = room.occupiedCount,
                            monthlyRent = room.monthlyRent,
                            amenities = room.amenities,
                            status = room.status,
                            isLoading = false
                        )
                    }
                }
        }
    }

    fun onRoomNumberChanged(value: String) {
        _uiState.value = _uiState.value.copy(
            roomNumber = value,
            roomNumberError = null,
            errorMessage = null
        )
    }

    fun onFloorChanged(value: Int) {
        if (value in 1..20) {
            _uiState.value = _uiState.value.copy(floor = value)
        }
    }

    fun onTypeChanged(type: String) {
        _uiState.value = _uiState.value.copy(type = type)
    }

    fun onCapacityChanged(value: String) {
        val capacity = value.toIntOrNull() ?: 0
        if (capacity in 1..50) {
            _uiState.value = _uiState.value.copy(
                capacity = capacity,
                capacityError = null
            )
        }
    }

    fun onOccupiedCountChanged(value: String) {
        val occupied = value.toIntOrNull() ?: 0
        _uiState.value = _uiState.value.copy(occupiedCount = occupied)
    }

    fun onMonthlyRentChanged(value: String) {
        val rent = value.toDoubleOrNull() ?: 0.0
        if (rent >= 0) {
            _uiState.value = _uiState.value.copy(
                monthlyRent = rent,
                rentError = null
            )
        }
    }

    fun onStatusChanged(status: String) {
        _uiState.value = _uiState.value.copy(status = status)
    }

    fun onAmenitiesChanged(amenities: List<String>) {
        _uiState.value = _uiState.value.copy(amenities = amenities)
    }

    fun onSaveClicked() {
        if (validateInput()) {
            saveRoom()
        }
    }

    private fun validateInput(): Boolean {
        val currentState = _uiState.value
        var hasError = false
        var errors = _uiState.value.copy()

        if (currentState.roomNumber.isBlank()) {
            errors = errors.copy(roomNumberError = "Room number is required")
            hasError = true
        }

        if (currentState.capacity < 1) {
            errors = errors.copy(capacityError = "Capacity must be at least 1")
            hasError = true
        }

        if (currentState.monthlyRent < 0) {
            errors = errors.copy(rentError = "Rent must be non-negative")
            hasError = true
        }

        if (hasError) {
            _uiState.value = errors
        }

        return !hasError
    }

    private fun saveRoom() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true)
            
            val currentState = _uiState.value
            val room = Room(
                id = currentState.roomId ?: UUID.randomUUID().toString(),
                roomNumber = currentState.roomNumber,
                floor = currentState.floor,
                type = currentState.type,
                capacity = currentState.capacity,
                occupiedCount = currentState.occupiedCount,
                monthlyRent = currentState.monthlyRent,
                amenities = currentState.amenities,
                status = currentState.status
            )
            
            val result: Result<Unit> = if (currentState.roomId != null) {
                updateRoomUseCase(room)
            } else {
                when (val addResult = addRoomUseCase(room)) {
                    is Result.Success -> Result.Success(Unit)
                    is Result.Error -> Result.Error(addResult.exception)
                    else -> Result.Loading
                }
            }
            
            when (result) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        saveSuccess = true,
                        errorMessage = null
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        errorMessage = result.exception.message ?: "Failed to save room"
                    )
                }
                else -> {}
            }
        }
    }
}
