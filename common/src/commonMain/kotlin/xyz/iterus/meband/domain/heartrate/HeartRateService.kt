package xyz.iterus.meband.domain.heartrate

import kotlinx.coroutines.flow.Flow

interface HeartRateService {

    suspend fun heartrate(): Flow<Int>
}
