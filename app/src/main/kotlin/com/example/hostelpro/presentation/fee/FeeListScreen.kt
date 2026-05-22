@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.hostelpro.presentation.fee

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.hostelpro.R

@Composable
fun FeeListScreen(
    navController: NavHostController,
    viewModel: FeeListViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    state.selectedFeeId?.let { feeId ->
        val fee = state.fees.find { it.id == feeId }
        AlertDialog(
            onDismissRequest = { viewModel.cancelPayment() },
            title = { Text("Record Payment") },
            text = {
                Column {
                    Text("Due: ₹${fee?.dueAmount?.toInt() ?: 0}")
                    OutlinedTextField(
                        value = state.paymentAmount,
                        onValueChange = viewModel::onPaymentAmountChanged,
                        label = { Text("Amount") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = { TextButton(onClick = { viewModel.recordPayment() }) { Text("Pay") } },
            dismissButton = { TextButton(onClick = { viewModel.cancelPayment() }) { Text(stringResource(R.string.cancel)) } }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.fees)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.addDemoFee() }) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.fees) { fee ->
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Month: ${fee.month}", style = MaterialTheme.typography.titleMedium)
                        Text("Total: ₹${fee.totalAmount.toInt()} | Paid: ₹${fee.paidAmount.toInt()}", style = MaterialTheme.typography.bodySmall)
                        Text("Status: ${fee.status}", style = MaterialTheme.typography.bodySmall)
                        if (fee.dueAmount > 0) {
                            Button(onClick = { viewModel.selectFeeForPayment(fee.id) }, modifier = Modifier.fillMaxWidth()) {
                                Text("Record Payment")
                            }
                        }
                    }
                }
            }
        }
    }
}
