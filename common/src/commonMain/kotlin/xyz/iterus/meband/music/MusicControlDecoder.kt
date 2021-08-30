package xyz.iterus.meband.music

import io.ktor.utils.io.core.*

internal class MusicControlDecoder {

    fun decode(musicEventBytes: ByteArray): MusicControlEvent? {
        if (musicEventBytes.size != 2) {
            println("Invalid music event packet size")
            return null
        }

        val musicEventPacket = ByteReadPacket(musicEventBytes)

        val header = musicEventPacket.readByte()

        if (header != HEADER_MUSIC_EVENT) {
            println("Not a music event!")
            return null
        }

        val musicEvent = musicEventPacket.readByte()

        return musicEvents.getOrElse(musicEvent, {
                MusicControlEvent.Unknown(musicEvent)
            }
        )
    }

    companion object {
        const val HEADER_MUSIC_EVENT = 0xFE.toByte()

        private val musicEvents = mapOf(
            0xE0.toByte() to MusicControlEvent.Start,
            0xE1.toByte() to MusicControlEvent.Finish,
            0x00.toByte() to MusicControlEvent.Play,
            0x01.toByte() to MusicControlEvent.Pause,
            0x03.toByte() to MusicControlEvent.Next,
            0x04.toByte() to MusicControlEvent.Previous,
            0x05.toByte() to MusicControlEvent.VolumeUp,
            0x06.toByte() to MusicControlEvent.VolumeDown,
        )
    }
}
