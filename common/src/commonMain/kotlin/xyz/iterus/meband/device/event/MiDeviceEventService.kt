package xyz.iterus.meband.device.event

import com.juul.kable.Peripheral
import com.juul.kable.characteristicOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.shareIn
import xyz.iterus.meband.bluetooth.MiBandConst
import xyz.iterus.meband.bluetooth.MiBandConst.SERVICE_MI_BAND

class MiDeviceEventService(
    private val peripheral: Peripheral,
    private val scope: CoroutineScope
) {

    private val deviceEventCh = characteristicOf(
        SERVICE_MI_BAND,
        CHARACTERISTIC_DEVICE_EVENT
    )

    val events = peripheral.observe(deviceEventCh)
        .flowOn(Dispatchers.Default)
        .shareIn(scope, SharingStarted.Eagerly)

    private companion object {
        const val CHARACTERISTIC_DEVICE_EVENT = "00000010-${MiBandConst.BASE_MI_UUID}"
    }
}
