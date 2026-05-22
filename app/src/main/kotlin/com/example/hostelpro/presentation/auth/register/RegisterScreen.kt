package com.example.hostelpro.presentation.auth.register

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.hostelpro.R
import com.example.hostelpro.presentation.common.components.ErrorCard
import com.example.hostelpro.presentation.navigation.Routes
import com.example.hostelpro.presentation.navigation.dashboardRouteForRole
import com.example.hostelpro.utils.Constants

@Composable
fun RegisterScreen(
    navController: NavHostController,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.registeredUser) {
        state.registeredUser?.let { user ->
            viewModel.onNavigationHandled()
            navController.navigate(Routes.DASHBOARD) {
                popUpTo(Routes.AUTH_GRAPH) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(40.dp))
        Text(stringResource(R.string.register_title), style = MaterialTheme.typography.displaySmall, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(24.dp))
        state.errorMessage?.let { ErrorCard(it); Spacer(Modifier.height(8.dp)) }
        OutlinedTextField(state.name, viewModel::onNameChanged, label = { Text(stringResource(R.string.name_hint)) }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(state.email, viewModel::onEmailChanged, label = { Text(stringResource(R.string.email_hint)) }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(state.phone, viewModel::onPhoneChanged, label = { Text(stringResource(R.string.phone_hint)) }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(state.password, viewModel::onPasswordChanged, label = { Text(stringResource(R.string.password_hint)) }, modifier = Modifier.fillMaxWidth(), visualTransformation = PasswordVisualTransformation())
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(state.confirmPassword, viewModel::onConfirmPasswordChanged, label = { Text("Confirm Password") }, modifier = Modifier.fillMaxWidth(), visualTransformation = PasswordVisualTransformation())
        Spacer(Modifier.height(12.dp))
        Text(stringResource(R.string.select_role), style = MaterialTheme.typography.labelMedium)
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)) {
            listOf(
                Constants.ROLE_ADMIN to stringResource(R.string.role_admin),
                Constants.ROLE_STAFF to stringResource(R.string.role_staff),
                Constants.ROLE_STUDENT to stringResource(R.string.role_student)
            ).forEach { (role, label) ->
                FilterChip(selected = state.role == role, onClick = { viewModel.onRoleChanged(role) }, label = { Text(label) })
            }
        }
        Spacer(Modifier.height(20.dp))
        Button(onClick = { viewModel.onRegisterClicked() }, modifier = Modifier.fillMaxWidth().height(48.dp), enabled = !state.isLoading) {
            Text(stringResource(R.string.register))
        }
        Spacer(Modifier.height(12.dp))
        Text(stringResource(R.string.login_button), color = MaterialTheme.colorScheme.primary, modifier = Modifier.clickable {
            navController.navigate(Routes.LOGIN) { popUpTo(Routes.REGISTER) { inclusive = true } }
        })
    }
}
