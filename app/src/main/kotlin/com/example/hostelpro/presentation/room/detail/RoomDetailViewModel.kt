package com.example.hostelpro.presentation.room.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hostelpro.domain.model.Room
import com.example.hostelpro.domain.usecase.room.DeleteRoomUseCase
import com.example.hostelpro.domain.usecase.room.GetRoomByIdUseCase
import com.example.hostelpro.utils.Result
import com.example.hostelpro.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RoomDetailUiState(
    val room: Room? = null,
    val uiState: UiState<Unit> = UiState.Loading,
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val isDeleting: Boolean = false,
    val deleteSuccess: Boolean = false
)

@HiltViewModel
class RoomDetailViewModel @Inject constructor(
    private val getRoomByIdUseCase: GetRoomByIdUseCase,
    private val deleteRoomUseCase: DeleteRoomUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    private val roomId: String = checkNotNull(savedStateHandle.get<String>("roomId"))
    
    private val _uiState = MutableStateFlow(RoomDetailUiState())
    val uiState: StateFlow<RoomDetailUiState> = _uiState.asStateFlow()

    init {
        loadRoom()
    }

    private fun loadRoom() {
        viewModelScope.launch {
            getRoomByIdUseCase(roomId)
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        uiState = UiState.Error(e),
                        errorMessage = e.message ?: "Failed to load room",
                        isLoading = false
                    )
                }
                .collect { result ->
                    when (result) {
                        is Result.Success -> {
                            _uiState.value = _uiState.value.copy(
                                room = result.data,
                                uiState = UiState.Success(Unit),
                                isLoading = false,
                                errorMessage = null
                            )
                        }
                        is Result.Error -> {
                            _uiState.value = _uiState.value.copy(
                                uiState = UiState.Error(result.exception),
                                errorMessage = result.exception.message ?: "Failed to load room",
                                isLoading = false
                            )
                        }
                        else -> {}
                    }
                }
        }
    }

    fun onDeleteClicked() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isDeleting = true)
            
            val result = deleteRoomUseCase(roomId)
            
            when (result) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isDeleting = false,
                        deleteSuccess = true
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isDeleting = false,
                        errorMessage = result.exception.message ?: "Failed to delete room"
                    )
                }
                else -> {}
            }
        }
    }

    fun onRetryClicked() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        loadRoom()
    }
}
