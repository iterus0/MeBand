package xyz.iterus.meband.mtu

import io.ktor.utils.io.core.*
import kotlin.experimental.or

internal class MTUEncoder(
    private val mtu: Int = 23
) {

    @ExperimentalIoApi
    fun encode(packet: ByteReadPacket, type: Int): List<ByteReadPacket> {
        val chunkLength = mtu - 6
        val chunks = mutableListOf<ByteReadPacket>()

        while (packet.canRead()) {
            chunks.add(
                ByteReadPacket(packet.readBytesOf(max = chunkLength))
            )
        }

        val packets = chunks.mapIndexed { chunkNumber, chunk ->
            var flags: Byte = if (chunks.size == 1) 0x40 else 0x00

            if (chunkNumber == chunks.lastIndex) {
                flags = flags or 0x80.toByte()
            } else {
                if (chunkNumber > 0) {
                    flags = flags or 0x40
                }
            }

            flags = flags or type.toByte()

            buildPacket {
                writeByte(0x00)
                writeByte(flags)
                writeByte(chunkNumber.toByte())
                writePacket(chunk)
            }
        }

        return packets
    }
}
