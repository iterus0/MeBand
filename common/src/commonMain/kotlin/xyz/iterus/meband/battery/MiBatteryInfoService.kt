package xyz.iterus.meband.battery

import com.juul.kable.Peripheral
import com.juul.kable.characteristicOf
import io.ktor.utils.io.core.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import xyz.iterus.meband.bluetooth.MiBandConst.BASE_MI_UUID
import xyz.iterus.meband.bluetooth.MiBandConst.SERVICE_MI_BAND
import xyz.iterus.meband.domain.battery.BatteryInfoService

class MiBatteryInfoService(
    private val peripheral: Peripheral
) : BatteryInfoService {

    private val batteryInfoCharacteristic = characteristicOf(
        SERVICE_MI_BAND,
        CHARACTERISTIC_BATTERY_INFO
    )

    override suspend fun batteryLevel(): Int = withContext(Dispatchers.Default) {
        //peripheral.connect()

        val batteryRawInfo = ByteReadPacket(peripheral.read(batteryInfoCharacteristic), 1, 1)
        val batteryPercentage = batteryRawInfo.readByte().toInt()

        batteryPercentage
    }

    companion object {
        const val CHARACTERISTIC_BATTERY_INFO = "00000006-$BASE_MI_UUID"
    }
}
