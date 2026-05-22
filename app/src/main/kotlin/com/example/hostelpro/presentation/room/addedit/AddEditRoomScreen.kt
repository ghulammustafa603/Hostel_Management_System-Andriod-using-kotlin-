@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.hostelpro.presentation.room.addedit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.hostelpro.R
import com.example.hostelpro.domain.model.RoomStatus
import com.example.hostelpro.domain.model.RoomType
import com.example.hostelpro.presentation.common.components.ErrorCard

@Composable
fun AddEditRoomScreen(
    navController: NavHostController,
    viewModel: AddEditRoomViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isEditMode = uiState.roomId != null

    if (uiState.saveSuccess) {
        navController.popBackStack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "Edit Room" else "Add Room") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (uiState.errorMessage != null) {
                ErrorCard(message = uiState.errorMessage!!)
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Room Number
            OutlinedTextField(
                value = uiState.roomNumber,
                onValueChange = { viewModel.onRoomNumberChanged(it) },
                label = { Text(stringResource(R.string.room_number)) },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.roomNumberError != null,
                supportingText = {
                    if (uiState.roomNumberError != null) {
                        Text(uiState.roomNumberError!!)
                    }
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Floor
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = uiState.floor.toString(),
                    onValueChange = { it.toIntOrNull()?.let { floor -> viewModel.onFloorChanged(floor) } },
                    label = { Text("Floor") },
                    modifier = Modifier
                        .weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )

                // Room Type Dropdown
                RoomTypeDropdown(
                    selectedType = uiState.type,
                    onTypeChanged = { viewModel.onTypeChanged(it) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Capacity
            OutlinedTextField(
                value = if (uiState.capacity > 0) uiState.capacity.toString() else "",
                onValueChange = { viewModel.onCapacityChanged(it) },
                label = { Text("Capacity") },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.capacityError != null,
                supportingText = {
                    if (uiState.capacityError != null) {
                        Text(uiState.capacityError!!)
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Occupied Count
            OutlinedTextField(
                value = uiState.occupiedCount.toString(),
                onValueChange = { viewModel.onOccupiedCountChanged(it) },
                label = { Text("Occupied Count") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Monthly Rent
            OutlinedTextField(
                value = if (uiState.monthlyRent > 0) uiState.monthlyRent.toString() else "",
                onValueChange = { viewModel.onMonthlyRentChanged(it) },
                label = { Text("Monthly Rent (₹)") },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.rentError != null,
                supportingText = {
                    if (uiState.rentError != null) {
                        Text(uiState.rentError!!)
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Room Status Dropdown
            RoomStatusDropdown(
                selectedStatus = uiState.status,
                onStatusChanged = { viewModel.onStatusChanged(it) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Text(stringResource(R.string.cancel))
                }

                Button(
                    onClick = { viewModel.onSaveClicked() },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    enabled = !uiState.isSaving && !uiState.isLoading
                ) {
                    Text(if (isEditMode) "Update" else "Create")
                }
            }
        }
    }
}

@Composable
private fun RoomTypeDropdown(
    selectedType: String,
    onTypeChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    
    Column(modifier = modifier) {
        OutlinedTextField(
            value = selectedType,
            onValueChange = {},
            label = { Text("Room Type") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            readOnly = true,
            enabled = false
        )

        if (expanded) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                RoomType.values().forEach { roomType ->
                    DropdownMenuItem(
                        text = { Text(roomType.displayName) },
                        onClick = {
                            onTypeChanged(roomType.name)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun RoomStatusDropdown(
    selectedStatus: String,
    onStatusChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    
    Column(modifier = modifier) {
        OutlinedTextField(
            value = selectedStatus,
            onValueChange = {},
            label = { Text("Room Status") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            readOnly = true,
            enabled = false
        )

        if (expanded) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                RoomStatus.values().forEach { status ->
                    DropdownMenuItem(
                        text = { Text(status.displayName) },
                        onClick = {
                            onStatusChanged(status.name)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
