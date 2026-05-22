package com.example.hostelpro.domain.repository

import com.example.hostelpro.domain.model.Room
import com.example.hostelpro.utils.Result
import kotlinx.coroutines.flow.Flow

interface RoomRepository {
    
    suspend fun addRoom(room: Room): Result<String>
    
    suspend fun updateRoom(room: Room): Result<Unit>
    
    suspend fun deleteRoom(roomId: String): Result<Unit>
    
    fun getRoomById(roomId: String): Flow<Result<Room?>>
    
    fun getAllRooms(): Flow<Result<List<Room>>>
    
    fun getRoomsByStatus(status: String): Flow<Result<List<Room>>>
    
    fun getRoomsByFloor(floor: Int): Flow<Result<List<Room>>>
    
    fun getRoomsByType(type: String): Flow<Result<List<Room>>>
    
    fun getTotalRoomCount(): Flow<Result<Int>>
    
    fun getOccupiedRoomCount(): Flow<Result<Int>>
    
    fun getAvailableRoomCount(): Flow<Result<Int>>
    
    suspend fun syncRoomsWithCloud(): Result<Unit>
}
