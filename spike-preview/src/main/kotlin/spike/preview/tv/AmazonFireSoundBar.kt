package spike.preview.tv

import spike.Bind
import spike.Include

@Include
@Bind(SoundSystem::class)
class AmazonFireSoundBar: SoundSystem {
    override var sound: Int = 15
        set(value) {
            field = value.coerceIn(0..100)
        }
}