package xyz.iterus.meband.device.music

import io.ktor.utils.io.core.*
import xyz.iterus.meband.domain.music.MusicInfo
import xyz.iterus.meband.domain.music.player.PlayerState
import kotlin.experimental.or

internal class MiMusicInfoEncoder {

    @ExperimentalIoApi
    fun encode(musicInfo: MusicInfo): ByteReadPacket {
        val isArtist = musicInfo.song.artist.isNotEmpty()
        val isAlbum = musicInfo.song.album.isNotEmpty()
        val isTrack = musicInfo.song.track.isNotEmpty()
        val isDuration = musicInfo.song.duration > 0

        // BitSet is not available
        var flags: Byte = 0x01

        if (isArtist) {
            flags = flags or ((1 shl 1).toByte())
        }
        if (isAlbum) {
            flags = flags or ((1 shl 2).toByte())
        }
        if (isTrack) {
            flags = flags or ((1 shl 3).toByte())
        }
        if (isDuration) {
            flags = flags or ((1 shl 4).toByte())
        }

        return buildPacket {
            val playState: Byte = when (musicInfo.player.state) {
                PlayerState.Playing -> 0x01
                else -> 0x00
            }
            val position: Short = musicInfo.player.position.toShort()

            writeByte(flags)
            writeByte(playState)
            writeByte(0x00)
            writeShort(position, ByteOrder.LITTLE_ENDIAN)

            if (isArtist) {
                writeText(musicInfo.song.artist)
                writeByte(0x00)
            }

            if (isAlbum) {
                writeText(musicInfo.song.album)
                writeByte(0x00)
            }

            if (isTrack) {
                writeText(musicInfo.song.track)
                writeByte(0x00)
            }

            if (isDuration) {
                writeShort(musicInfo.song.duration.toShort(), ByteOrder.LITTLE_ENDIAN)
            }
        }
    }
}
