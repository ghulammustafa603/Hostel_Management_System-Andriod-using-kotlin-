package com.example.hostelpro.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.hostelpro.data.local.entity.RoomEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoom(room: RoomEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRooms(rooms: List<RoomEntity>)
    
    @Update
    suspend fun updateRoom(room: RoomEntity)
    
    @Delete
    suspend fun deleteRoom(room: RoomEntity)
    
    @Query("SELECT * FROM rooms WHERE id = :roomId")
    fun getRoomById(roomId: String): Flow<RoomEntity?>
    
    @Query("SELECT * FROM rooms ORDER BY roomNumber ASC")
    fun getAllRooms(): Flow<List<RoomEntity>>
    
    @Query("SELECT * FROM rooms WHERE status = :status ORDER BY roomNumber ASC")
    fun getRoomsByStatus(status: String): Flow<List<RoomEntity>>
    
    @Query("SELECT * FROM rooms WHERE floor = :floor ORDER BY roomNumber ASC")
    fun getRoomsByFloor(floor: Int): Flow<List<RoomEntity>>
    
    @Query("SELECT * FROM rooms WHERE type = :type ORDER BY roomNumber ASC")
    fun getRoomsByType(type: String): Flow<List<RoomEntity>>
    
    @Query("SELECT COUNT(*) FROM rooms")
    fun getTotalRoomCount(): Flow<Int>
    
    @Query("SELECT COUNT(*) FROM rooms WHERE status = 'OCCUPIED'")
    fun getOccupiedRoomCount(): Flow<Int>
    
    @Query("SELECT COUNT(*) FROM rooms WHERE status = 'AVAILABLE'")
    fun getAvailableRoomCount(): Flow<Int>
}
