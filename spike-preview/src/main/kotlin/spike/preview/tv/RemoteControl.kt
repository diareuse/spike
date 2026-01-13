package spike.preview.tv

import spike.Bind
import spike.Include
import spike.Provider

@Include
@Bind(Remote::class)
class RemoteControl(
    private val televisionProvider: Provider<Television>,
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