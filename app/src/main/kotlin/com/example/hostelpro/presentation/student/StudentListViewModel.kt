package com.example.hostelpro.presentation.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hostelpro.domain.model.Student
import com.example.hostelpro.domain.repository.StudentRepository
import com.example.hostelpro.utils.Constants
import com.example.hostelpro.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class StudentListUiState(
    val students: List<Student> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val showAddDialog: Boolean = false,
    val newName: String = "",
    val newEmail: String = "",
    val newPhone: String = ""
)

@HiltViewModel
class StudentListViewModel @Inject constructor(
    private val studentRepository: StudentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StudentListUiState())
    val uiState: StateFlow<StudentListUiState> = _uiState.asStateFlow()

    init { loadStudents() }

    private fun loadStudents() {
        viewModelScope.launch {
            studentRepository.getAllStudents().collect { result ->
                when (result) {
                    is Result.Success -> _uiState.value = _uiState.value.copy(
                        students = result.data, isLoading = false, errorMessage = null
                    )
                    is Result.Error -> _uiState.value = _uiState.value.copy(
                        isLoading = false, errorMessage = result.exception.message
                    )
                    else -> {}
                }
            }
        }
    }

    fun showAddDialog() { _uiState.value = _uiState.value.copy(showAddDialog = true) }
    fun hideAddDialog() { _uiState.value = _uiState.value.copy(showAddDialog = false) }
    fun onNewNameChanged(v: String) { _uiState.value = _uiState.value.copy(newName = v) }
    fun onNewEmailChanged(v: String) { _uiState.value = _uiState.value.copy(newEmail = v) }
    fun onNewPhoneChanged(v: String) { _uiState.value = _uiState.value.copy(newPhone = v) }

    fun addStudent() {
        val s = _uiState.value
        if (s.newName.isBlank() || s.newEmail.isBlank()) {
            _uiState.value = s.copy(errorMessage = "Name and email required")
            return
        }
        viewModelScope.launch {
            val student = Student(
                id = UUID.randomUUID().toString(),
                name = s.newName.trim(),
                email = s.newEmail.trim().lowercase(),
                phone = s.newPhone.ifBlank { "0000000000" },
                guardianName = "N/A",
                guardianPhone = "0000000000",
                roomId = null,
                joiningDate = System.currentTimeMillis(),
                checkOutDate = null,
                feeStatus = Constants.FEE_STATUS_DUE,
                photoUrl = null,
                status = Constants.STUDENT_STATUS_ACTIVE
            )
            when (studentRepository.addStudent(student)) {
                is Result.Success -> _uiState.value = _uiState.value.copy(
                    showAddDialog = false, newName = "", newEmail = "", newPhone = "", errorMessage = null
                )
                is Result.Error -> _uiState.value = _uiState.value.copy(errorMessage = "Failed to add student")
                else -> {}
            }
        }
    }
}
