package com.example.hostelpro.presentation.auth.forgot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hostelpro.domain.repository.AuthRepository
import com.example.hostelpro.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ForgotPasswordUiState(
    val email: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val success: Boolean = false
)

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState.asStateFlow()

    fun onEmailChanged(v: String) { _uiState.value = _uiState.value.copy(email = v, errorMessage = null) }
    fun onNewPasswordChanged(v: String) { _uiState.value = _uiState.value.copy(newPassword = v, errorMessage = null) }
    fun onConfirmPasswordChanged(v: String) { _uiState.value = _uiState.value.copy(confirmPassword = v, errorMessage = null) }

    fun onResetClicked() {
        val s = _uiState.value
        when {
            s.email.isBlank() -> _uiState.value = s.copy(errorMessage = "Enter your email")
            s.newPassword.length < 6 -> _uiState.value = s.copy(errorMessage = "Password must be at least 6 characters")
            s.newPassword != s.confirmPassword -> _uiState.value = s.copy(errorMessage = "Passwords do not match")
            else -> viewModelScope.launch {
                _uiState.value = s.copy(isLoading = true, errorMessage = null)
                when (val result = authRepository.resetPassword(s.email, s.newPassword)) {
                    is Result.Success -> _uiState.value = _uiState.value.copy(isLoading = false, success = true)
                    is Result.Error -> _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.exception.message
                    )
                    else -> _uiState.value = _uiState.value.copy(isLoading = false)
                }
            }
        }
    }
}
