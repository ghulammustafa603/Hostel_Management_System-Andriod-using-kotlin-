package com.example.hostelpro.data.repository

import com.example.hostelpro.data.local.dao.RoomDao
import com.example.hostelpro.data.local.mapper.RoomMapper
import com.example.hostelpro.domain.model.Room
import com.example.hostelpro.domain.repository.RoomRepository
import com.example.hostelpro.utils.Result
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RoomRepositoryImpl @Inject constructor(
    private val roomDao: RoomDao,
    private val firestore: FirebaseFirestore
) : RoomRepository {
    
    override suspend fun addRoom(room: Room): Result<String> {
        return try {
            val roomId = room.id.ifEmpty { firestore.collection("rooms").document().id }
            val roomWithId = room.copy(id = roomId)
            
            // Save to local database
            roomDao.insertRoom(RoomMapper.toEntity(roomWithId))
            
            syncRoomToFirestore(roomWithId)
            
            Result.Success(roomId)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    override suspend fun updateRoom(room: Room): Result<Unit> {
        return try {
            val updatedRoom = room.copy(updatedAt = System.currentTimeMillis())
            
            // Update local database
            roomDao.updateRoom(RoomMapper.toEntity(updatedRoom))
            
            // Sync with Firestore
            syncRoomToFirestore(updatedRoom)
            
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    override suspend fun deleteRoom(roomId: String): Result<Unit> {
        return try {
            // Delete from local database
            val room = roomDao.getRoomById(roomId).first()
            if (room != null) roomDao.deleteRoom(room)
            
            try {
                firestore.collection("rooms").document(roomId).delete().await()
            } catch (_: Exception) { /* offline / demo mode */ }
            
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    override fun getRoomById(roomId: String): Flow<Result<Room?>> {
        return roomDao.getRoomById(roomId)
            .map { entity ->
                try {
                    Result.Success(entity?.let { RoomMapper.toDomain(it) })
                } catch (e: Exception) {
                    Result.Error(e)
                }
            }
            .catch { e ->
                emit(Result.Error(e))
            }
    }
    
    override fun getAllRooms(): Flow<Result<List<Room>>> {
        return roomDao.getAllRooms()
            .map { entities ->
                try {
                    Result.Success(RoomMapper.toDomainList(entities))
                } catch (e: Exception) {
                    Result.Error(e)
                }
            }
            .catch { e ->
                emit(Result.Error(e))
            }
    }
    
    override fun getRoomsByStatus(status: String): Flow<Result<List<Room>>> {
        return roomDao.getRoomsByStatus(status)
            .map { entities ->
                try {
                    Result.Success(RoomMapper.toDomainList(entities))
                } catch (e: Exception) {
                    Result.Error(e)
                }
            }
            .catch { e ->
                emit(Result.Error(e))
            }
    }
    
    override fun getRoomsByFloor(floor: Int): Flow<Result<List<Room>>> {
        return roomDao.getRoomsByFloor(floor)
            .map { entities ->
                try {
                    Result.Success(RoomMapper.toDomainList(entities))
                } catch (e: Exception) {
                    Result.Error(e)
                }
            }
            .catch { e ->
                emit(Result.Error(e))
            }
    }
    
    override fun getRoomsByType(type: String): Flow<Result<List<Room>>> {
        return roomDao.getRoomsByType(type)
            .map { entities ->
                try {
                    Result.Success(RoomMapper.toDomainList(entities))
                } catch (e: Exception) {
                    Result.Error(e)
                }
            }
            .catch { e ->
                emit(Result.Error(e))
            }
    }
    
    override fun getTotalRoomCount(): Flow<Result<Int>> {
        return roomDao.getTotalRoomCount()
            .map<Int, Result<Int>> { count -> Result.Success(count) }
            .catch { e -> emit(Result.Error(e)) }
    }
    
    override fun getOccupiedRoomCount(): Flow<Result<Int>> {
        return roomDao.getOccupiedRoomCount()
            .map<Int, Result<Int>> { count -> Result.Success(count) }
            .catch { e -> emit(Result.Error(e)) }
    }
    
    override fun getAvailableRoomCount(): Flow<Result<Int>> {
        return roomDao.getAvailableRoomCount()
            .map<Int, Result<Int>> { count -> Result.Success(count) }
            .catch { e -> emit(Result.Error(e)) }
    }
    
    private suspend fun syncRoomToFirestore(room: Room) {
        try {
            firestore.collection("rooms").document(room.id).set(room).await()
        } catch (_: Exception) { /* offline / demo mode */ }
    }

    override suspend fun syncRoomsWithCloud(): Result<Unit> {
        return try {
            val snapshot = firestore.collection("rooms").get().await()
            val rooms = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Room::class.java)?.copy(id = doc.id)
            }
            
            roomDao.insertRooms(rooms.map { RoomMapper.toEntity(it) })
            
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
