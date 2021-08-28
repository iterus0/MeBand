package xyz.iterus.meband.battery

interface BatteryInfoService {

    suspend fun batteryLevel(): Int
}
