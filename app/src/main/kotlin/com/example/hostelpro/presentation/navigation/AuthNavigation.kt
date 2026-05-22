package com.example.hostelpro.presentation.navigation

import com.example.hostelpro.domain.model.User
import com.example.hostelpro.utils.Constants

fun dashboardRouteForRole(@Suppress("UNUSED_PARAMETER") role: String): String = Routes.DASHBOARD

fun User.dashboardRoute(): String = dashboardRouteForRole(role)
