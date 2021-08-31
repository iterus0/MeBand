package xyz.iterus.meband.android

import android.bluetooth.BluetoothManager
import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.juul.kable.Peripheral
import com.juul.kable.peripheral
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import xyz.iterus.meband.device.MiBand
import xyz.iterus.meband.device.battery.MiBatteryInfoService
import xyz.iterus.meband.device.event.MiDeviceEventService
import xyz.iterus.meband.device.heartrate.GenericBleHeartRateService
import xyz.iterus.meband.device.music.MiMusicControlService

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var miband: Peripheral

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val batteryTv: TextView = findViewById(R.id.batteryLevel)
        val heartrateTv: TextView = findViewById(R.id.heartrate)

        lifecycleScope.launchWhenStarted {
            println("[Job] Started")
            val mibandId = "XX:XX:XX:XX:XX:XX"
            val bleManager =
                applicationContext.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager
            val adapter = bleManager?.adapter
                ?: throw UnsupportedOperationException("Bluetooth is not supported!")

            val btd = adapter.getRemoteDevice(mibandId)
            miband = peripheral(btd) {
                logging {
                    //level = Logging.Level.Events
                }
            }
            miband.connect()

            val batInfoService = MiBatteryInfoService(miband)
            val hrService = GenericBleHeartRateService(miband)
            val deviceEventService = MiDeviceEventService(miband, lifecycleScope)
            val musicControlService = MiMusicControlService(miband, lifecycleScope, deviceEventService)
            val mbs = MiBand(miband, batInfoService, hrService, musicControlService)

            batteryTv.text = "${mbs.batteryLevel()} %"

            val hr = async {
                mbs.heartrate()
                    .collect {
                        println("[HR] $it")
                        heartrateTv.text = "$it BPM"
                    }
            }

            val mus = async {
                mbs.musicEvent()
                    .collect {
                        println("[Music] $it")
                        heartrateTv.text = "$it"
                    }
            }

            hr.await()
            mus.await()
        }.invokeOnCompletion {
            println("[Job] Completed")
        }
    }

    override fun onStop() {
        super.onStop()

        lifecycleScope.launch {
            miband.disconnect()
        }
    }
}
