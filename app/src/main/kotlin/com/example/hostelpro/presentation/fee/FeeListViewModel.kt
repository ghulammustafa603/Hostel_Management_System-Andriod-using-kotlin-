package com.example.hostelpro.presentation.fee

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hostelpro.domain.model.Fee
import com.example.hostelpro.domain.repository.FeeRepository
import com.example.hostelpro.utils.Constants
import com.example.hostelpro.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class FeeListUiState(
    val fees: List<Fee> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val selectedFeeId: String? = null,
    val paymentAmount: String = ""
)

@HiltViewModel
class FeeListViewModel @Inject constructor(
    private val feeRepository: FeeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeeListUiState())
    val uiState: StateFlow<FeeListUiState> = _uiState.asStateFlow()

    init { loadFees() }

    private fun loadFees() {
        viewModelScope.launch {
            feeRepository.getAllFees().collect { result ->
                when (result) {
                    is Result.Success -> _uiState.value = _uiState.value.copy(fees = result.data, isLoading = false)
                    is Result.Error -> _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = result.exception.message)
                    else -> {}
                }
            }
        }
    }

    fun selectFeeForPayment(feeId: String) {
        _uiState.value = _uiState.value.copy(selectedFeeId = feeId, paymentAmount = "")
    }

    fun onPaymentAmountChanged(v: String) {
        _uiState.value = _uiState.value.copy(paymentAmount = v)
    }

    fun cancelPayment() {
        _uiState.value = _uiState.value.copy(selectedFeeId = null, paymentAmount = "")
    }

    fun recordPayment() {
        val feeId = _uiState.value.selectedFeeId ?: return
        val amount = _uiState.value.paymentAmount.toDoubleOrNull() ?: return
        viewModelScope.launch {
            when (feeRepository.recordPayment(feeId, amount, Constants.PAYMENT_MODE_CASH)) {
                is Result.Success -> _uiState.value = _uiState.value.copy(selectedFeeId = null, paymentAmount = "")
                is Result.Error -> _uiState.value = _uiState.value.copy(errorMessage = "Payment failed")
                else -> {}
            }
        }
    }

    fun addDemoFee() {
        viewModelScope.launch {
            feeRepository.addFee(
                Fee(
                    id = UUID.randomUUID().toString(),
                    studentId = "student-1",
                    month = "2026-06",
                    rentAmount = 8000.0,
                    messAmount = 3000.0,
                    otherCharges = 0.0,
                    totalAmount = 11000.0,
                    paidAmount = 0.0,
                    paymentDate = null,
                    paymentMode = null,
                    status = Constants.FEE_STATUS_DUE
                )
            )
        }
    }
}
