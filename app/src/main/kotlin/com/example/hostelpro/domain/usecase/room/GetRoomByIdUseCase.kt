package com.example.hostelpro.domain.usecase.room

import com.example.hostelpro.domain.model.Room
import com.example.hostelpro.domain.repository.RoomRepository
import com.example.hostelpro.utils.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRoomByIdUseCase @Inject constructor(
    private val repository: RoomRepository
) {
    operator fun invoke(roomId: String): Flow<Result<Room?>> {
        return repository.getRoomById(roomId)
    }
}
