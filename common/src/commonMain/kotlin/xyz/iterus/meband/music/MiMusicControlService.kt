package xyz.iterus.meband.music

import com.juul.kable.Peripheral
import com.juul.kable.characteristicOf
import io.ktor.utils.io.core.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import xyz.iterus.meband.bluetooth.MiBandConst
import xyz.iterus.meband.bluetooth.MiBandConst.SERVICE_MI_BAND
import xyz.iterus.meband.domain.music.MusicControlService
import xyz.iterus.meband.domain.music.MusicInfo
import xyz.iterus.meband.domain.music.player.PlayerState
import xyz.iterus.meband.domain.music.player.PlayerStatus
import xyz.iterus.meband.domain.music.song.SongInfo
import xyz.iterus.meband.event.MiDeviceEventService
import xyz.iterus.meband.extension.toHexString
import xyz.iterus.meband.mtu.MTUEncoder

class MiMusicControlService(
    private val peripheral: Peripheral,
    private val scope: CoroutineScope,
    private val deviceEventService: MiDeviceEventService
) : MusicControlService {

    private val musicDecoder = MusicControlDecoder()
    private val musicInfoEncoder = MiMusicInfoEncoder()
    private val mtuEncoder = MTUEncoder()

    private val transferCh = characteristicOf(
        SERVICE_MI_BAND,
        CHARACTERISTIC_CHUNKED_TRANSFER
    )

    override fun musicEvent() = deviceEventService.events
        .filter { it.firstOrNull() == MusicControlDecoder.HEADER_MUSIC_EVENT }
        .mapNotNull { musicDecoder.decode(it) }
        .flowOn(Dispatchers.Default)

    private val musicEventHandler = musicEvent().onEach { musicControlEvent ->
        when (musicControlEvent) {
            MusicControlEvent.Start -> {
                val testComposition = MusicInfo(
                    SongInfo("Talented artist", "Great album", "Amazing track", 128),
                    PlayerStatus(64, PlayerState.Playing)
                )

                val musicPacket = musicInfoEncoder.encode(testComposition)

                val packets = mtuEncoder.encode(musicPacket, 3)

                packets.forEach { packet ->
                    val bytes = packet.readBytes()
                    println("[send] ${bytes.toHexString()}")
                    peripheral.write(transferCh, bytes)
                }
            }
            else -> {
            }
        }
    }.shareIn(scope, SharingStarted.Eagerly)

    private companion object {
        const val CHARACTERISTIC_CHUNKED_TRANSFER = "00000020-${MiBandConst.BASE_MI_UUID}"
    }
}
