package com.example.hostelpro.domain.usecase.room

import com.example.hostelpro.domain.repository.RoomRepository
import com.example.hostelpro.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

data class RoomStats(
    val totalRooms: Int,
    val occupiedRooms: Int,
    val availableRooms: Int
)

class GetRoomStatsUseCase @Inject constructor(
    private val repository: RoomRepository
) {
    operator fun invoke(): Flow<Result<RoomStats>> {
        return combine(
            repository.getTotalRoomCount(),
            repository.getOccupiedRoomCount(),
            repository.getAvailableRoomCount()
        ) { totalResult, occupiedResult, availableResult ->
            when {
                totalResult is Result.Error -> totalResult
                occupiedResult is Result.Error -> occupiedResult
                availableResult is Result.Error -> availableResult
                totalResult is Result.Success && occupiedResult is Result.Success && availableResult is Result.Success -> {
                    Result.Success(
                        RoomStats(
                            totalRooms = totalResult.data,
                            occupiedRooms = occupiedResult.data,
                            availableRooms = availableResult.data
                        )
                    )
                }
                else -> Result.Loading
            }
        }
    }
}
