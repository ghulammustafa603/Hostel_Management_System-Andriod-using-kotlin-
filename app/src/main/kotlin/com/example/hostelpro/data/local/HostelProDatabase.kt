package com.example.hostelpro.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.hostelpro.data.local.dao.ComplaintDao
import com.example.hostelpro.data.local.dao.CredentialDao
import com.example.hostelpro.data.local.dao.FeeDao
import com.example.hostelpro.data.local.dao.RoomDao
import com.example.hostelpro.data.local.dao.StudentDao
import com.example.hostelpro.data.local.dao.UserDao
import com.example.hostelpro.data.local.dao.VisitorDao
import com.example.hostelpro.data.local.entity.ComplaintEntity
import com.example.hostelpro.data.local.entity.CredentialEntity
import com.example.hostelpro.data.local.entity.FeeEntity
import com.example.hostelpro.data.local.entity.RoomEntity
import com.example.hostelpro.data.local.entity.StudentEntity
import com.example.hostelpro.data.local.entity.UserEntity
import com.example.hostelpro.data.local.entity.VisitorEntity

@Database(
    entities = [
        RoomEntity::class,
        StudentEntity::class,
        FeeEntity::class,
        ComplaintEntity::class,
        VisitorEntity::class,
        UserEntity::class,
        CredentialEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class HostelProDatabase : RoomDatabase() {
    
    abstract fun roomDao(): RoomDao
    abstract fun studentDao(): StudentDao
    abstract fun feeDao(): FeeDao
    abstract fun complaintDao(): ComplaintDao
    abstract fun visitorDao(): VisitorDao
    abstract fun userDao(): UserDao
    abstract fun credentialDao(): CredentialDao
    
    companion object {
        @Volatile
        private var INSTANCE: HostelProDatabase? = null
        
        fun getInstance(context: Context): HostelProDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HostelProDatabase::class.java,
                    "hostelpro_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
