package xyz.iterus.meband.domain.music

import kotlinx.coroutines.flow.Flow
import xyz.iterus.meband.music.MusicControlEvent

interface MusicControlService {

    fun musicEvent(): Flow<MusicControlEvent>
}
