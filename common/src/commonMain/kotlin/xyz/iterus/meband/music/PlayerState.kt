package xyz.iterus.meband.music

sealed class PlayerState {
    object Playing : PlayerState()
    object Paused : PlayerState()
    object Stopped : PlayerState()
}
