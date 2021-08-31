package xyz.iterus.meband.device.heartrate

import com.juul.kable.Peripheral
import com.juul.kable.characteristicOf
import io.ktor.utils.io.core.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import xyz.iterus.meband.bluetooth.BleConst.BASE_UUID
import xyz.iterus.meband.domain.heartrate.HeartRateService

class GenericBleHeartRateService(
    private val peripheral: Peripheral
) : HeartRateService {

    private val heartrateCharacteristic = characteristicOf(
        SERVICE_HEART_RATE,
        CHARACTERISTIC_HEART_RATE
    )

    override suspend fun heartrate() = flow {
        //peripheral.connect()

        peripheral.observe(heartrateCharacteristic)
            .map { ByteReadPacket(it) }
            .collect { heartRateData ->
                val hr = heartRateData.readShort().toInt()
                emit(hr)
            }
    }.flowOn(Dispatchers.Default)

    companion object {
        private const val SERVICE_HEART_RATE = "0000180D-$BASE_UUID"
        private const val CHARACTERISTIC_HEART_RATE = "00002A37-$BASE_UUID"
    }
}
