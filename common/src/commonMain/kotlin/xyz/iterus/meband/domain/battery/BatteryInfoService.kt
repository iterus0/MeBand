package xyz.iterus.meband.domain.battery

interface BatteryInfoService {

    suspend fun batteryLevel(): Int
}
