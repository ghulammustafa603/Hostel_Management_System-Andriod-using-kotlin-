package com.example.hostelpro.data.local.mapper

import com.example.hostelpro.data.local.entity.RoomEntity
import com.example.hostelpro.domain.model.Room
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

object RoomMapper {
    
    fun toDomain(entity: RoomEntity): Room {
        val amenities = try {
            Json.decodeFromString<List<String>>(entity.amenities)
        } catch (e: Exception) {
            emptyList()
        }
        
        return Room(
            id = entity.id,
            roomNumber = entity.roomNumber,
            floor = entity.floor,
            type = entity.type,
            capacity = entity.capacity,
            occupiedCount = entity.occupiedCount,
            monthlyRent = entity.monthlyRent,
            amenities = amenities,
            status = entity.status,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }
    
    fun toEntity(room: Room): RoomEntity {
        val amenitiesJson = Json.encodeToString(room.amenities)
        
        return RoomEntity(
            id = room.id,
            roomNumber = room.roomNumber,
            floor = room.floor,
            type = room.type,
            capacity = room.capacity,
            occupiedCount = room.occupiedCount,
            monthlyRent = room.monthlyRent,
            amenities = amenitiesJson,
            status = room.status,
            createdAt = room.createdAt,
            updatedAt = room.updatedAt
        )
    }
    
    fun toDomainList(entities: List<RoomEntity>): List<Room> {
        return entities.map { toDomain(it) }
    }
}
