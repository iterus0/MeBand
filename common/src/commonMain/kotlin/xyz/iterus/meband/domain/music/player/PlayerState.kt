package xyz.iterus.meband.domain.music.player

sealed class PlayerState {
    object Playing : PlayerState()
    object Paused : PlayerState()
    object Stopped : PlayerState()
}
