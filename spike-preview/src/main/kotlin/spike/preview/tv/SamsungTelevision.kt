package spike.preview.tv

import spike.Bind
import spike.Include
import spike.Singleton

@Singleton
@Include
@Bind(Television::class)
class SamsungTelevision(
    private val soundSystem: Lazy<SoundSystem>
) : Television {
    override var playing: Boolean = false
    override var channel: Int = 1
        set(value) {
            field = value.coerceIn(1..99)
        }
    override var sound: Int
        get() = soundSystem.value.sound
        set(value) {
            soundSystem.value.sound = value
        }
    override val currentEpg: String
        get() = "EPG for channel $channel"
}