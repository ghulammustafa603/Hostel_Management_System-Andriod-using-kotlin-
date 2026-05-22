package com.example.hostelpro.data.local

import android.content.Context
import com.example.hostelpro.data.local.dao.ComplaintDao
import com.example.hostelpro.data.local.dao.CredentialDao
import com.example.hostelpro.data.local.dao.FeeDao
import com.example.hostelpro.data.local.dao.RoomDao
import com.example.hostelpro.data.local.dao.StudentDao
import com.example.hostelpro.data.local.dao.UserDao
import com.example.hostelpro.data.local.entity.ComplaintEntity
import com.example.hostelpro.data.local.entity.CredentialEntity
import com.example.hostelpro.data.local.entity.FeeEntity
import com.example.hostelpro.data.local.entity.RoomEntity
import com.example.hostelpro.data.local.entity.StudentEntity
import com.example.hostelpro.data.local.entity.UserEntity
import com.example.hostelpro.utils.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SeedDataInitializer @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userDao: UserDao,
    private val credentialDao: CredentialDao,
    private val roomDao: RoomDao,
    private val studentDao: StudentDao,
    private val feeDao: FeeDao,
    private val complaintDao: ComplaintDao
) {
    private val prefs = context.getSharedPreferences("hostelpro_seed", Context.MODE_PRIVATE)

    fun seedIfNeeded() {
        if (prefs.getInt(KEY_SEED_VERSION, 0) >= SEED_VERSION) return
        CoroutineScope(Dispatchers.IO).launch {
            seedDemoAccounts()
            seedRooms()
            seedStudents()
            seedFees()
            seedComplaints()
            prefs.edit().putInt(KEY_SEED_VERSION, SEED_VERSION).apply()
        }
    }

    private suspend fun seedDemoAccounts() {
        val accounts = listOf(
            DemoAccount("admin-1", "Admin User", "admin@hostel.com", "03001234567", "admin123", Constants.ROLE_ADMIN),
            DemoAccount("staff-1", "Staff User", "staff@hostel.com", "03007654321", "staff123", Constants.ROLE_STAFF),
            DemoAccount("student-1", "Ali Khan", "student@hostel.com", "03009876543", "student123", Constants.ROLE_STUDENT)
        )
        accounts.forEach { account ->
            userDao.insertUser(
                UserEntity(
                    id = account.id,
                    name = account.name,
                    email = account.email,
                    phone = account.phone,
                    role = account.role,
                    profilePhotoUrl = null
                )
            )
            credentialDao.insertCredential(
                CredentialEntity(email = account.email, password = account.password, userId = account.id)
            )
        }
    }

    private suspend fun seedRooms() {
        val rooms = listOf(
            RoomEntity("room-101", "101", 1, "DOUBLE", 2, 1, 8000.0, "WiFi,AC", "OCCUPIED"),
            RoomEntity("room-102", "102", 1, "SINGLE", 1, 0, 6000.0, "WiFi", "AVAILABLE"),
            RoomEntity("room-201", "201", 2, "TRIPLE", 3, 2, 5000.0, "WiFi,Fan", "OCCUPIED"),
            RoomEntity("room-202", "202", 2, "DORM", 6, 4, 3500.0, "WiFi", "OCCUPIED")
        )
        roomDao.insertRooms(rooms)
    }

    private suspend fun seedStudents() {
        val students = listOf(
            StudentEntity(
                id = "student-1",
                name = "Ali Khan",
                email = "student@hostel.com",
                phone = "03009876543",
                guardianName = "Ahmed Khan",
                guardianPhone = "03001112222",
                roomId = "room-101",
                joiningDate = System.currentTimeMillis(),
                checkOutDate = null,
                feeStatus = Constants.FEE_STATUS_PARTIAL,
                photoUrl = null,
                status = Constants.STUDENT_STATUS_ACTIVE
            ),
            StudentEntity(
                id = "student-2",
                name = "Sara Ahmed",
                email = "sara@hostel.com",
                phone = "03003334444",
                guardianName = "Fatima Ahmed",
                guardianPhone = "03005556666",
                roomId = "room-201",
                joiningDate = System.currentTimeMillis(),
                checkOutDate = null,
                feeStatus = Constants.FEE_STATUS_DUE,
                photoUrl = null,
                status = Constants.STUDENT_STATUS_ACTIVE
            )
        )
        studentDao.insertStudents(students)
    }

    private suspend fun seedFees() {
        val fees = listOf(
            FeeEntity(
                id = "fee-1",
                studentId = "student-1",
                month = "2026-05",
                rentAmount = 8000.0,
                messAmount = 3000.0,
                otherCharges = 500.0,
                totalAmount = 11500.0,
                paidAmount = 5000.0,
                paymentDate = System.currentTimeMillis(),
                paymentMode = Constants.PAYMENT_MODE_UPI,
                status = Constants.FEE_STATUS_PARTIAL
            ),
            FeeEntity(
                id = "fee-2",
                studentId = "student-2",
                month = "2026-05",
                rentAmount = 5000.0,
                messAmount = 3000.0,
                otherCharges = 0.0,
                totalAmount = 8000.0,
                paidAmount = 0.0,
                paymentDate = null,
                paymentMode = null,
                status = Constants.FEE_STATUS_DUE
            )
        )
        feeDao.insertFees(fees)
    }

    private suspend fun seedComplaints() {
        complaintDao.insertComplaint(
            ComplaintEntity(
                id = "complaint-1",
                studentId = "student-1",
                roomId = "room-101",
                category = "Maintenance",
                description = "AC not cooling properly in room 101",
                photoUrl = null,
                status = Constants.COMPLAINT_STATUS_OPEN,
                createdAt = System.currentTimeMillis(),
                resolvedAt = null,
                assignedStaffId = "staff-1"
            )
        )
    }

    private data class DemoAccount(
        val id: String,
        val name: String,
        val email: String,
        val phone: String,
        val password: String,
        val role: String
    )

    companion object {
        private const val KEY_SEED_VERSION = "seed_version"
        private const val SEED_VERSION = 2
    }
}
