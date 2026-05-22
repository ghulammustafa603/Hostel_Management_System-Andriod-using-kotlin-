package com.example.hostelpro.domain.repository

import com.example.hostelpro.domain.model.User
import com.example.hostelpro.utils.Result

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun register(
        name: String,
        email: String,
        phone: String,
        password: String,
        role: String
    ): Result<User>
    suspend fun resetPassword(email: String, newPassword: String): Result<Unit>
    fun getCurrentUser(): User?
    fun logout()
    fun isLoggedIn(): Boolean
}
