package com.example.hostelpro.di

import android.content.Context
import com.example.hostelpro.data.local.HostelProDatabase
import com.example.hostelpro.data.local.dao.ComplaintDao
import com.example.hostelpro.data.local.dao.CredentialDao
import com.example.hostelpro.data.local.dao.FeeDao
import com.example.hostelpro.data.local.dao.RoomDao
import com.example.hostelpro.data.local.dao.StudentDao
import com.example.hostelpro.data.local.dao.UserDao
import com.example.hostelpro.data.local.dao.VisitorDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Singleton
    @Provides
    fun provideHostelProDatabase(
        @ApplicationContext context: Context
    ): HostelProDatabase {
        return HostelProDatabase.getInstance(context)
    }
    
    @Singleton
    @Provides
    fun provideRoomDao(database: HostelProDatabase): RoomDao {
        return database.roomDao()
    }
    
    @Singleton
    @Provides
    fun provideStudentDao(database: HostelProDatabase): StudentDao {
        return database.studentDao()
    }
    
    @Singleton
    @Provides
    fun provideFeeDao(database: HostelProDatabase): FeeDao {
        return database.feeDao()
    }
    
    @Singleton
    @Provides
    fun provideComplaintDao(database: HostelProDatabase): ComplaintDao {
        return database.complaintDao()
    }
    
    @Singleton
    @Provides
    fun provideVisitorDao(database: HostelProDatabase): VisitorDao {
        return database.visitorDao()
    }
    
    @Singleton
    @Provides
    fun provideUserDao(database: HostelProDatabase): UserDao {
        return database.userDao()
    }

    @Singleton
    @Provides
    fun provideCredentialDao(database: HostelProDatabase): CredentialDao {
        return database.credentialDao()
    }
}
