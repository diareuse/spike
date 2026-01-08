package spike.preview.tv

import spike.*

@EntryPoint
interface NightStand {
    val television: Television
    val remote: Remote
    companion object
}