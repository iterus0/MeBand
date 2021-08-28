package xyz.iterus.meband.heartrate

import kotlinx.coroutines.flow.Flow

interface HeartRateService {

    suspend fun heartrate(): Flow<Int>
}
