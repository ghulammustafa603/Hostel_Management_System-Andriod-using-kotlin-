package com.example.hostelpro.domain.usecase.room

import com.example.hostelpro.domain.repository.RoomRepository
import com.example.hostelpro.utils.Result
import javax.inject.Inject

class DeleteRoomUseCase @Inject constructor(
    private val repository: RoomRepository
) {
    suspend operator fun invoke(roomId: String): Result<Unit> {
        return repository.deleteRoom(roomId)
    }
}
