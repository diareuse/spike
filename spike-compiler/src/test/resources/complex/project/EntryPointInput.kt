@spike.EntryPoint
interface NightStand {
    val television: Television
    val remote: Remote

    @spike.EntryPoint.Factory
    interface Factory {
        fun create(batteries: List<Battery>): NightStand
    }

    companion object
}

@spike.Include
@spike.Bind(SoundSystem::class)
class AmazonFireSoundBar: SoundSystem {
    override var sound: Int = 15
        set(value) {
            field = value.coerceIn(0..100)
        }
}

interface Battery

interface Remote {
    fun togglePower()
    fun channelUp()
    fun channelDown()
    fun volumeUp()
    fun volumeDown()
}

@spike.Include
@spike.Bind(Remote::class)
class RemoteControl(
    private val televisionProvider: spike.Provider<Television>,
    @Suppress("unused")
    private val batteries: List<Battery>
) : Remote {

    private var television: Television? = null

    override fun togglePower() {
        val tv = television
        if (tv?.playing == true) {
            tv.playing = false
            this.television = null
        } else {
            this.television = televisionProvider.get().also {
                it.playing = true
            }
        }
    }

    override fun channelUp() {
        television?.channel++
    }

    override fun channelDown() {
        television?.channel--
    }

    override fun volumeUp() {
        television?.sound++
    }

    override fun volumeDown() {
        television?.sound--
    }
}

@spike.Singleton
@spike.Include
@spike.Bind(Television::class)
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

interface SoundSystem {
    var sound: Int
}

interface Television {
    var playing: Boolean
    var channel: Int
    var sound: Int
    val currentEpg: String
}