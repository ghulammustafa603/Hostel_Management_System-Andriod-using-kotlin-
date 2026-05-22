package com.example.hostelpro.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.hostelpro.domain.repository.AuthRepository
import com.example.hostelpro.utils.Constants

@Composable
fun HostelProNavigation(
    authRepository: AuthRepository,
    navController: NavHostController = rememberNavController()
) {
    val startDestination = remember {
        if (authRepository.isLoggedIn()) Routes.DASHBOARD else Routes.AUTH_GRAPH
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        authGraph(navController)
        dashboardGraph(navController)
        roomGraph(navController)
        studentGraph(navController)
        feeGraph(navController)
        complaintGraph(navController)
    }
}
