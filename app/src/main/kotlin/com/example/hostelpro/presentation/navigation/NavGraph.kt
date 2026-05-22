package com.example.hostelpro.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.hostelpro.presentation.auth.forgot.ForgotPasswordScreen
import com.example.hostelpro.presentation.auth.login.LoginScreen
import com.example.hostelpro.presentation.auth.register.RegisterScreen
import com.example.hostelpro.presentation.complaint.ComplaintListScreen
import com.example.hostelpro.presentation.dashboard.DashboardScreen
import com.example.hostelpro.presentation.fee.FeeListScreen
import com.example.hostelpro.presentation.room.addedit.AddEditRoomScreen
import com.example.hostelpro.presentation.room.detail.RoomDetailScreen
import com.example.hostelpro.presentation.room.list.RoomListScreen
import com.example.hostelpro.presentation.student.StudentListScreen

object Routes {
    const val AUTH_GRAPH = "auth_graph"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val FORGOT_PASSWORD = "forgot_password"

    const val DASHBOARD = "dashboard"
    const val ADMIN_DASHBOARD = "dashboard"
    const val STAFF_DASHBOARD = "dashboard"
    const val STUDENT_DASHBOARD = "dashboard"

    const val ROOM_GRAPH = "room_graph"
    const val ROOM_LIST = "room_list"
    const val ROOM_DETAIL = "room_detail/{roomId}"
    const val ADD_ROOM = "add_room"
    const val EDIT_ROOM = "edit_room/{roomId}"

    const val STUDENT_GRAPH = "student_graph"
    const val STUDENT_LIST = "student_list"

    const val FEE_GRAPH = "fee_graph"
    const val FEE_LIST = "fee_list"

    const val COMPLAINT_GRAPH = "complaint_graph"
    const val COMPLAINT_LIST = "complaint_list"
}

fun NavGraphBuilder.authGraph(navController: NavHostController) {
    navigation(route = Routes.AUTH_GRAPH, startDestination = Routes.LOGIN) {
        composable(Routes.LOGIN) { LoginScreen(navController) }
        composable(Routes.REGISTER) { RegisterScreen(navController) }
        composable(Routes.FORGOT_PASSWORD) { ForgotPasswordScreen(navController) }
    }
}

fun NavGraphBuilder.dashboardGraph(navController: NavHostController) {
    composable(Routes.DASHBOARD) { DashboardScreen(navController) }
}

fun NavGraphBuilder.roomGraph(navController: NavHostController) {
    composable(Routes.ROOM_LIST) { RoomListScreen(navController) }
    composable(
        route = Routes.ROOM_DETAIL,
        arguments = listOf(navArgument("roomId") { type = NavType.StringType })
    ) { RoomDetailScreen(navController) }
    composable(Routes.ADD_ROOM) { AddEditRoomScreen(navController) }
    composable(
        route = Routes.EDIT_ROOM,
        arguments = listOf(navArgument("roomId") { type = NavType.StringType })
    ) { AddEditRoomScreen(navController) }
}

fun NavGraphBuilder.studentGraph(navController: NavHostController) {
    composable(Routes.STUDENT_LIST) { StudentListScreen(navController) }
}

fun NavGraphBuilder.feeGraph(navController: NavHostController) {
    composable(Routes.FEE_LIST) { FeeListScreen(navController) }
}

fun NavGraphBuilder.complaintGraph(navController: NavHostController) {
    composable(Routes.COMPLAINT_LIST) { ComplaintListScreen(navController) }
}
