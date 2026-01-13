package spike.preview.tv

import spike.EntryPoint

@EntryPoint
interface NightStand {
    val television: Television
    val remote: Remote

    @EntryPoint.Factory
    interface Factory {
        fun create(batteries: List<Battery>): NightStand
    }

    companion object
}