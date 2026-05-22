package com.example.hostelpro.presentation.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hostelpro.domain.model.User
import com.example.hostelpro.domain.repository.AuthRepository
import com.example.hostelpro.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val loggedInUser: User? = null
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChanged(email: String) {
        _uiState.value = _uiState.value.copy(email = email, errorMessage = null)
    }

    fun onPasswordChanged(password: String) {
        _uiState.value = _uiState.value.copy(password = password, errorMessage = null)
    }

    fun onPasswordVisibilityToggle() {
        _uiState.value = _uiState.value.copy(
            isPasswordVisible = !_uiState.value.isPasswordVisible
        )
    }

    fun onLoginClicked() {
        val current = _uiState.value
        when {
            current.email.isBlank() -> _uiState.value = current.copy(errorMessage = "Please enter your email")
            current.password.isBlank() -> _uiState.value = current.copy(errorMessage = "Please enter your password")
            else -> performLogin(current.email, current.password)
        }
    }

    fun onGoogleSignInClicked() {
        _uiState.value = _uiState.value.copy(
            errorMessage = "Google Sign-In requires Firebase setup. Use demo: admin@hostel.com / admin123"
        )
    }

    fun onNavigationHandled() {
        _uiState.value = _uiState.value.copy(loggedInUser = null)
    }

    private fun performLogin(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            when (val result = authRepository.login(email, password)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        loggedInUser = result.data
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.exception.message ?: "Login failed"
                    )
                }
                else -> _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
}
