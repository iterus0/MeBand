package xyz.iterus.meband.device.music

sealed class MusicControlEvent {
    object Start : MusicControlEvent()
    object Finish : MusicControlEvent()

    object Play : MusicControlEvent()
    object Pause : MusicControlEvent()
    object Next : MusicControlEvent()
    object Previous : MusicControlEvent()

    object VolumeUp : MusicControlEvent()
    object VolumeDown : MusicControlEvent()

    data class Unknown(val event: Byte) : MusicControlEvent()
}
