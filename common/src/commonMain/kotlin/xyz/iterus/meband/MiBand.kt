package xyz.iterus.meband

import com.juul.kable.Peripheral
import xyz.iterus.meband.domain.battery.BatteryInfoService
import xyz.iterus.meband.battery.MiBatteryInfoService
import xyz.iterus.meband.heartrate.GenericBleHeartRateService
import xyz.iterus.meband.domain.heartrate.HeartRateService
import xyz.iterus.meband.music.MiMusicControlService
import xyz.iterus.meband.domain.music.MusicControlService

class MiBand(
    private val peripheral: Peripheral,
    private val miBatteryInfoService: MiBatteryInfoService,
    private val heartrateService: GenericBleHeartRateService,
    private val miMusicControlService: MiMusicControlService
) : BatteryInfoService by miBatteryInfoService,
    HeartRateService by heartrateService,
    MusicControlService by miMusicControlService {

    init {
        //peripheral.connect()
    }
}
