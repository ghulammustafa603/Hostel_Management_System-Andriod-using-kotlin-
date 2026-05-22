package com.example.hostelpro.domain.usecase.room

import com.example.hostelpro.domain.model.Room
import com.example.hostelpro.domain.repository.RoomRepository
import com.example.hostelpro.utils.Result
import javax.inject.Inject

class UpdateRoomUseCase @Inject constructor(
    private val repository: RoomRepository
) {
    suspend operator fun invoke(room: Room): Result<Unit> {
        return repository.updateRoom(room)
    }
}
