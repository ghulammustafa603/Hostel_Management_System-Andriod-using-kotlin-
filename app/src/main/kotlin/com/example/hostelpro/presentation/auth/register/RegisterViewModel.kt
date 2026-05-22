package com.example.hostelpro.presentation.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hostelpro.domain.model.User
import com.example.hostelpro.domain.repository.AuthRepository
import com.example.hostelpro.utils.Constants
import com.example.hostelpro.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegisterUiState(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val role: String = Constants.ROLE_STUDENT,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val registeredUser: User? = null
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onNameChanged(v: String) { _uiState.value = _uiState.value.copy(name = v, errorMessage = null) }
    fun onEmailChanged(v: String) { _uiState.value = _uiState.value.copy(email = v, errorMessage = null) }
    fun onPhoneChanged(v: String) { _uiState.value = _uiState.value.copy(phone = v, errorMessage = null) }
    fun onPasswordChanged(v: String) { _uiState.value = _uiState.value.copy(password = v, errorMessage = null) }
    fun onConfirmPasswordChanged(v: String) { _uiState.value = _uiState.value.copy(confirmPassword = v, errorMessage = null) }
    fun onRoleChanged(role: String) { _uiState.value = _uiState.value.copy(role = role) }

    fun onNavigationHandled() {
        _uiState.value = _uiState.value.copy(registeredUser = null)
    }

    fun onRegisterClicked() {
        val s = _uiState.value
        when {
            s.name.isBlank() -> _uiState.value = s.copy(errorMessage = "Enter your name")
            s.email.isBlank() -> _uiState.value = s.copy(errorMessage = "Enter your email")
            s.phone.isBlank() -> _uiState.value = s.copy(errorMessage = "Enter your phone")
            s.password.length < 6 -> _uiState.value = s.copy(errorMessage = "Password must be at least 6 characters")
            s.password != s.confirmPassword -> _uiState.value = s.copy(errorMessage = "Passwords do not match")
            else -> viewModelScope.launch {
                _uiState.value = s.copy(isLoading = true, errorMessage = null)
                when (val result = authRepository.register(s.name, s.email, s.phone, s.password, s.role)) {
                    is Result.Success -> _uiState.value = _uiState.value.copy(isLoading = false, registeredUser = result.data)
                    is Result.Error -> _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.exception.message ?: "Registration failed"
                    )
                    else -> _uiState.value = _uiState.value.copy(isLoading = false)
                }
            }
        }
    }
}
