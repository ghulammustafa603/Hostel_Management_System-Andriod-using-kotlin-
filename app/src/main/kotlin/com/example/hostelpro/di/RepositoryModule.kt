package com.example.hostelpro.di

import com.example.hostelpro.data.repository.AuthRepositoryImpl
import com.example.hostelpro.data.repository.ComplaintRepositoryImpl
import com.example.hostelpro.data.repository.FeeRepositoryImpl
import com.example.hostelpro.data.repository.RoomRepositoryImpl
import com.example.hostelpro.data.repository.StudentRepositoryImpl
import com.example.hostelpro.domain.repository.AuthRepository
import com.example.hostelpro.domain.repository.ComplaintRepository
import com.example.hostelpro.domain.repository.FeeRepository
import com.example.hostelpro.domain.repository.RoomRepository
import com.example.hostelpro.domain.repository.StudentRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Singleton
    @Binds
    abstract fun bindRoomRepository(
        impl: RoomRepositoryImpl
    ): RoomRepository

    @Singleton
    @Binds
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Singleton
    @Binds
    abstract fun bindStudentRepository(
        impl: StudentRepositoryImpl
    ): StudentRepository

    @Singleton
    @Binds
    abstract fun bindFeeRepository(
        impl: FeeRepositoryImpl
    ): FeeRepository

    @Singleton
    @Binds
    abstract fun bindComplaintRepository(
        impl: ComplaintRepositoryImpl
    ): ComplaintRepository
}
