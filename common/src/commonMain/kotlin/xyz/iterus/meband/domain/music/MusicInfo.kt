package xyz.iterus.meband.domain.music

import xyz.iterus.meband.domain.music.player.PlayerStatus
import xyz.iterus.meband.domain.music.song.SongInfo

internal data class MusicInfo(
    val song: SongInfo,
    val player: PlayerStatus
)
