package xyz.iterus.meband.music

import kotlinx.coroutines.flow.Flow

interface MusicControlService {

    fun musicEvent(): Flow<MusicControlEvent>
}
