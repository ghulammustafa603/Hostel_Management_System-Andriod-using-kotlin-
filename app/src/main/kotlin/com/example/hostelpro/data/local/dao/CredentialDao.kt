package com.example.hostelpro.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.hostelpro.data.local.entity.CredentialEntity

@Dao
interface CredentialDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCredential(credential: CredentialEntity)

    @Query("SELECT * FROM credentials WHERE email = :email LIMIT 1")
    suspend fun getByEmail(email: String): CredentialEntity?

    @Query("UPDATE credentials SET password = :password WHERE email = :email")
    suspend fun updatePassword(email: String, password: String)
}
