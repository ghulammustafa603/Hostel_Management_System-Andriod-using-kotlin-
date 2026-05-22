package com.example.hostelpro.data.repository

import com.example.hostelpro.data.local.SessionManager
import com.example.hostelpro.data.local.dao.CredentialDao
import com.example.hostelpro.data.local.dao.UserDao
import com.example.hostelpro.data.local.entity.CredentialEntity
import com.example.hostelpro.data.local.entity.UserEntity
import com.example.hostelpro.domain.model.User
import com.example.hostelpro.domain.repository.AuthRepository
import com.example.hostelpro.utils.Result
import kotlinx.coroutines.flow.first
import java.util.UUID
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val credentialDao: CredentialDao,
    private val sessionManager: SessionManager
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val normalizedEmail = email.trim().lowercase()
            val credential = credentialDao.getByEmail(normalizedEmail)
                ?: return Result.Error(Exception("No account found. Please register first."))

            if (credential.password != password) {
                return Result.Error(Exception("Incorrect password"))
            }

            val userEntity = userDao.getUserById(credential.userId).first()
                ?: return Result.Error(Exception("User profile not found"))

            val user = userEntity.toDomain()
            sessionManager.saveSession(user.id, user.email, user.role, user.name)
            Result.Success(user)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun register(
        name: String,
        email: String,
        phone: String,
        password: String,
        role: String
    ): Result<User> {
        return try {
            val normalizedEmail = email.trim().lowercase()
            if (credentialDao.getByEmail(normalizedEmail) != null) {
                return Result.Error(Exception("Email already registered"))
            }

            val userId = UUID.randomUUID().toString()
            val userEntity = UserEntity(
                id = userId,
                name = name.trim(),
                email = normalizedEmail,
                phone = phone.trim(),
                role = role,
                profilePhotoUrl = null
            )
            userDao.insertUser(userEntity)
            credentialDao.insertCredential(
                CredentialEntity(
                    email = normalizedEmail,
                    password = password,
                    userId = userId
                )
            )

            val user = userEntity.toDomain()
            sessionManager.saveSession(user.id, user.email, user.role, user.name)
            Result.Success(user)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun resetPassword(email: String, newPassword: String): Result<Unit> {
        return try {
            val normalizedEmail = email.trim().lowercase()
            val credential = credentialDao.getByEmail(normalizedEmail)
                ?: return Result.Error(Exception("No account found with this email"))
            credentialDao.updatePassword(normalizedEmail, newPassword)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override fun getCurrentUser(): User? {
        if (!sessionManager.isLoggedIn()) return null
        val id = sessionManager.getUserId() ?: return null
        return User(
            id = id,
            name = sessionManager.getName() ?: "",
            email = sessionManager.getEmail() ?: "",
            phone = "",
            role = sessionManager.getRole() ?: ""
        )
    }

    override fun logout() {
        sessionManager.clearSession()
    }

    override fun isLoggedIn(): Boolean = sessionManager.isLoggedIn()

    private fun UserEntity.toDomain() = User(
        id = id,
        name = name,
        email = email,
        phone = phone,
        role = role,
        profilePhotoUrl = profilePhotoUrl
    )
}
