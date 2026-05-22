package com.example.hostelpro.domain.model

data class Room(
    val id: String,
    val roomNumber: String,
    val floor: Int,
    val type: String,  // SINGLE, DOUBLE, TRIPLE, DORM
    val capacity: Int,
    val occupiedCount: Int,
    val monthlyRent: Double,
    val amenities: List<String>,
    val status: String,  // AVAILABLE, OCCUPIED, MAINTENANCE
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    val availableCount: Int
        get() = capacity - occupiedCount
    
    val occupancyPercentage: Float
        get() = (occupiedCount.toFloat() / capacity) * 100f
    
    val isAvailable: Boolean
        get() = status == "AVAILABLE" && availableCount > 0
}

enum class RoomType(val displayName: String) {
    SINGLE("Single Occupancy"),
    DOUBLE("Double Occupancy"),
    TRIPLE("Triple Occupancy"),
    DORM("Dormitory");
    
    companion object {
        fun fromString(value: String): RoomType {
            return try {
                valueOf(value)
            } catch (e: Exception) {
                SINGLE
            }
        }
    }
}

enum class RoomStatus(val displayName: String) {
    AVAILABLE("Available"),
    OCCUPIED("Occupied"),
    MAINTENANCE("Maintenance");
    
    companion object {
        fun fromString(value: String): RoomStatus {
            return try {
                valueOf(value)
            } catch (e: Exception) {
                AVAILABLE
            }
        }
    }
}
